var app = angular.module('app', ['ngAnimate', 'ngTouch', '720kb.datepicker']);

app.config(['$compileProvider',
    function ($compileProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|tel|file|blob):/);
    }]);

app.controller('AppController', ['$scope', '$http', '$window',
    function ($scope, $http, $window) {

    $scope.migrate = function(migration) {

        //Default values
        if (!migration.from) {
            migration.from = "filmaffinity";
        }
        if (!migration.to) {
            migration.to = "csv";
        }

        var result = angular.element(document.querySelector('#migration-result'));
        $scope.result = "Loading ... \n";
        result.removeClass('success').removeClass('alert').addClass('info');
        var api_url = $window.location.origin + '/migrate';
        $http.post(api_url, migration)
            .success(function(data) {
                $scope.sourceStatus = data.sourceStatus;
                $scope.targetStatus = data.targetStatus;
                $scope.moviesReaded = data.moviesReaded;
                $scope.moviesWrited = data.moviesWrited;
                $scope.topDirector = data.topDirector;
                $scope.topActor = data.topActor;
                $scope.topCountry = data.topCountry;
                $scope.topYear = data.topYear;
                $scope.ratingAvg = data.ratingAvg;
                if ($scope.sourceStatus == true && $scope.targetStatus == true) {
                    if (migration.to == "csv") {
                        var blob = new Blob([data.csv], {type: 'text/csv'});
                        var downloadLink = angular.element('<a></a>');
                        downloadLink.attr('href', window.URL.createObjectURL(blob));
                        downloadLink.attr('download', migration.from + '-ratings.csv');
                        downloadLink[0].click();

                        $scope.result = "CSV succesfully generated: Found "+$scope.moviesReaded+ " movies";
                    }
                    else {
                        if (migration.to == "analysis") {
                            var analysis = angular.element(document.querySelector('#analysis-result'));
                            analysis.removeClass('ng-hide');
                            $scope.result+= "Source movies: "+$scope.moviesReaded;
                        }
                        else {
                            $scope.result+= "Source movies: "+$scope.moviesReaded+" - Matched movies on target: "+$scope.moviesWrited;
                        }
                    }

                }
                else {
                    $scope.result = "Login error";
                    result.removeClass('success').addClass('alert');
                }
                result.removeClass('alert').removeClass('info').addClass('success');
            })
            .error(function(data) {
                $scope.result = data.message;
                result.removeClass('success').removeClass('info').addClass('alert');
            });
    };

    $scope.anySelected = function (object) {
        return Object.keys(object).some(function (key) {
            return object[key];
        });
    };

}]);
