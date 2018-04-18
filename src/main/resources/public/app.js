var app = angular.module('app', ['ngAnimate', 'ngTouch', '720kb.datepicker']);

app.config(['$compileProvider',
    function ($compileProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|tel|file|blob):/);
    }]);

app.filter('rawHtml', ['$sce', function($sce){
    return function(val) {
        return $sce.trustAsHtml(val);
    };
}]);

app.controller('AppController', ['$scope', '$http', '$window',
    function ($scope, $http, $window) {

    $scope.migrate = function(migration) {

        //Default values
        if (!migration.from) {
            migration.from = "filmaffinity";
        }
        if (!migration.to) {
            migration.to = "analysis";
        }

        var result = angular.element(document.querySelector('#migration-result'));
        var analysis = angular.element(document.querySelector('#analysis-result'));
        analysis.addClass("ng-hide");

        $scope.result = "Loading ... \n";
        result.removeClass('success').removeClass('alert').removeClass('ng-hide').addClass('info');
        var api_url = $window.location.origin + '/migrate';
        $http.post(api_url, migration)
            .success(function(data) {
                $scope.sourceStatus = data.sourceStatus;
                $scope.targetStatus = data.targetStatus;
                $scope.moviesReaded = data.moviesReaded;
                $scope.moviesWrited = data.moviesWrited;
                $scope.ratingAvg = data.ratingAvg;
                $scope.analytics = data.analyticsComplex;

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
                            analysis.removeClass('ng-hide');
                            $scope.result= +$scope.moviesReaded + " ratings with avg: "+$scope.ratingAvg;
                        }
                        else {
                            $scope.result= "Source movies: "+$scope.moviesReaded+" - Matched movies on target: "+$scope.moviesWrited;
                        }
                    }

                }
                else {
                    $scope.result = "Login error";
                    result.removeClass('success').addClass('alert');
                }
                result.removeClass('alert').removeClass('info').addClass('success');

                $scope.loadYearHighCharts($scope.analytics);
                $scope.loadYearByRatingDateHighCharts($scope.analytics);
                $scope.loadRatingHighCharts($scope.analytics);
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

    $scope.getKeys = function(obj) {
        if (obj)
            return Object.keys(obj);
    };

    $scope.calculateAverage = function(MyData){
        var sum = 0;
        for(var i = 0; i < MyData.length; i++){
            sum += parseInt(MyData[i].rating, 10); //don't forget to add the base
        }
        return sum === 0 ? 0 : Math.round(sum*100.0/MyData.length) / 100;
    };

    $scope.getLength = function(obj){
        if (obj)
            return Object.keys(obj).length;
    };

    $scope.loadYearHighCharts = function(analytics) {

        Highcharts.chart('containerYear', {
            chart: {
                type: 'column'
            },
            title: {
                text: ''
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                column: {
                    borderRadius: 2
                },
                series: {
                    pointWidth: 10
                }
            },
            yAxis: [{
                className: 'highcharts-color-0',
                opposite: true,
                title: {
                    text: ''
                }
            }],
            xAxis: {
                categories: analytics.yearsChartKeys
            },
            series: [{
                name: "Movies",
                data: analytics.yearsChartValues
            }]

        });

    };

    $scope.loadYearByRatingDateHighCharts = function(analytics) {

            Highcharts.chart('containerYearByRatingDate', {
                chart: {
                    type: 'column'
                },
                title: {
                    text: ''
                },
                legend: {
                    enabled: false
                },
                plotOptions: {
                    column: {
                        borderRadius: 2
                    },
                    series: {
                        minPointWidth: 25,
                        maxPointWidth: 80
                    }
                },
                yAxis: [{
                    className: 'highcharts-color-0',
                    opposite: true,
                    title: {
                        text: ''
                    }
                }],
                xAxis: {
                    categories: analytics.yearsByRatingDateChartKeys
                },
                series: [{
                    name: "Movies",
                    data: analytics.yearsByRatingDateChartValues
                }]

            });

        };

    $scope.loadRatingHighCharts = function(analytics) {

            Highcharts.chart('containerRating', {
                chart: {
                    type: 'pie'
                },
                title: {
                    text: ''
                },
                series: [{
                    name: "Ratings",
                    type: 'pie',
                    allowPointSelect: true,
                    keys: ['name', 'y'],
                    data: analytics.ratingChart,
                    showInLegend: false
                }]

            });

        }
}]);
