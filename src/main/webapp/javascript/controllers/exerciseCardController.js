angular.module('trainingTrackerApp')

    .controller('exerciseCardCtrl', ['$scope', '$state', function ($scope, $state) {
        // visual description variables of the exercise cards
        $scope.exerciseName = $scope.exercise.name;
        $scope.exerciseMG = $scope.exercise.muscleGroup;
        $scope.exerciseWeight = $scope.exercise.weight;
        $scope.exerciseSeries = $scope.exercise.series;
        $scope.exerciseRepetitions = $scope.exercise.repetitions;
        $scope.exerciseId = $scope.exercise.id;

        // function that launches when the plus is clicked and open the add record modal
        $scope.addRecord = function ()  {
            $scope.hideSuccess();
            $scope.hideError();
            $scope.openRecordModal($scope.exercise);
        }

    }]);
