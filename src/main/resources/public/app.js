var app = angular.module('app', ['ngAnimate', 'ngTouch']);

app.config(['$compileProvider',
    function ($compileProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|tel|file|blob):/);
    }]);

app.controller('AppController', ['$scope', '$http',
    function ($scope, $http) {

    $scope.migrate = function(migration) {

        $http.post('http://localhost:8090/migrate', migration)
            .success(function(data) {
                $scope.sourceStatus = data.sourceStatus;
                $scope.targetStatus = data.targetStatus;
                $scope.moviesReaded = data.moviesReaded;
                $scope.moviesWrited = data.moviesWrited;
                $scope.ratingAvg = data.ratingAvg;
                if (migration.to == "csv") {
                    var blob = new Blob([data.csv], { type : 'text/csv' });
                    var downloadLink = angular.element('<a></a>');
                    downloadLink.attr('href',window.URL.createObjectURL(blob));
                    downloadLink.attr('download', migration.from+'-ratings.csv');
                    downloadLink[0].click();
                }
            });
    };

    $scope.$watch( 'sourceStatus',
        function(newValue, oldValue){
            console.log('sourceStatus Changed');
            console.log(newValue);
            console.log(oldValue);
        }
    );

}]);
