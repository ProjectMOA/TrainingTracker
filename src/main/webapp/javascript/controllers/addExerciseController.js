angular.module('trainingTrackerApp')

    .controller('addExerciseCtrl', ['$scope', '$state', 'exerciseService', function ($scope, $state, exerciseService) {

        $scope.predeterminedExercises = [];    // predetermined exercises
        $scope.predeterminedMG = [];    // predetermined muscle groups
        $scope.predeterminedCardioExercises = [];    // predetermined cardio exercises
        $scope.predeterminedCardioTypes = [];    // predetermined cardio types

        $scope.getPredetermined = function () {
            exerciseService.getPredetermined(function (predeterminedMG,predeterminedExercises, cardioTypes, cardioExercises) {
                $scope.predeterminedMG = predeterminedMG;
                $scope.predeterminedExercises = predeterminedExercises;
                $scope.predeterminedCardioTypes = cardioTypes;
                $scope.predeterminedCardioExercises = cardioExercises;
            },showError);
        };

        // options selected from the selectors html
        $scope.selectedPredetermined;
        $scope.cardioNameSelected;
        $scope.cardioTypeSelected;
        $scope.mgPredeterminedSelected;
        $scope.mgCustomSelected;
        $scope.customName;

        // feedback handling variables
        $scope.error = false;
        $scope.success = false;
        $scope.successMsg = "";
        $scope.errorMsg = "";

        // hide the error mensage
        $scope.hideError = function () {
            $scope.errorMsg = "";
            $scope.error = false;
        };
        // show the error mensage
        var showError = function (error) {
            $scope.errorMsg = error;
            $scope.error = true;
        };

        // show the success mensage
        var showSuccess = function (message) {
            $scope.successMsg = message;
            $scope.success = true;
        };

        // hide the success mensage
        $scope.hideSuccess = function () {
            $scope.success = false;
            $scope.successMsg = "";
        };

        $scope.getPredetermined();

        $scope.addPredetermined = function () {
            exerciseService.addExercise($scope.selectedPredetermined,showSuccess,showError);
        };
        $scope.addCustom = function () {
            var customExercise = {
                id: 0,
                muscleGroup: $scope.mgCustomSelected,
                name: $scope.customName
            };
            exerciseService.addExercise(customExercise,showSuccess,showError);
        };
        $scope.addCardiovascular = function () {
            exerciseService.addCardiovascular($scope.cardioNameSelected,showSuccess,showError);
        };

        // INPUTS CONTROL LENGTH

        $scope.nameMaxLength = false;

        // Watches to control input variables length
        $scope.$watch('customName', function () {
            if ($scope.customName != undefined) {
                if ($scope.customName.length > 50) {
                    $scope.customName = $scope.customName.slice(0, 50);
                } else if ($scope.customName.length == 50) {
                    $scope.nameMaxLength = true;
                } else {
                    $scope.nameMaxLength = false;
                }
            } else {
                $scope.nameMaxLength = false;
            }
        });
    }]);
