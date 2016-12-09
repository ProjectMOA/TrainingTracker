angular.module('trainingTrackerApp')

    .controller('exerciseHistoryCtrl', ['$scope', '$state', function ($scope, $state) {

        $scope.nameExercise = "Press Banca";
        $scope.recordHistory = [
            {weight: 1, series: 1, repetitions: 1, commentary: "blablabla", date: "aa"},
            {weight: 2, series: 2, repetitions: 2, commentary: "blublublu", date: "uu"},
            {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "ii"},
            {weight: 4, series: 4, repetitions: 4, commentary: "blebleble", date: "ee"}
        ];

        // sort the [list] array alphabetically by date
        function sortCustom (list) {
            list.sort(function(a, b){
                var x = a.date.toLowerCase();
                var y = b.date.toLowerCase();
                if (x < y) {return -1;}
                if (x > y) {return 1;}
                return 0;
            });
        }

        sortCustom($scope.recordHistory);
    }]);

