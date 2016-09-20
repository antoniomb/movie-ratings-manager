var app = angular.module('app', ['ngAnimate', 'ngTouch']);

app.controller('MainCtrl', ['$scope', '$http',
    function ($scope, $http) {

    $scope.migrate = function(migration) {

        //TODO hardcoded
        migration.from = "fa";
        migration.to = "letscine";

        $http.post('http://localhost:8090/migrate', migration)
            .success(function(data) {
                $scope.sourceStatus = data.sourceStatus;
                $scope.targetStatus = data.targetStatus;
                $scope.moviesReaded = data.moviesReaded;
                $scope.moviesWrited = data.moviesWrited;
                $scope.ratingAvg = data.ratingAvg;
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
