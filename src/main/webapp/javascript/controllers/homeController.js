angular.module('trainingTrackerApp')

    .controller('homeCtrl', ['$scope', '$state', 'exerciseService',
        function ($scope, $state, exerciseService) {

            $scope.exercisesList = []; //exercise list
            // exercise list server request
            $scope.getExercisesList = function () {
                exerciseService.getExercisesList(function (exercises) {
                    $scope.exercisesList = exercises;
                },showError);
            };
            $scope.getExercisesList();

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

            // MODAL SECTION

            $scope.recordModal = {    // temporal record data on modals
                id: 0,
                weight: "",
                series: "",
                repetitions: "",
                commentary: ""
            };

            // open modal with [record] information
            $scope.openRecordModal = function (exercise) {
                $("#recordModal").modal("show");
                $scope.recordModal = {
                    id: exercise.id
                };
            };

            // save record
            $scope.saveRecord = function () {
                $("#recordModal").modal("hide");
                $scope.save = true; //flag to indicate that we are saving the record
                $("#recordModal").on('hidden.bs.modal', function () {
                    if ($scope.save) {
                        exerciseService.saveRecord($scope.recordModal, showSuccess, showError,
                            function (exercises) {
                                $scope.exercisesList = exercises;
                            });
                        $scope.save = false;
                        $scope.recordModal = {
                            id: 0,
                            weight: "",
                            series: "",
                            repetitions: "",
                            commentary: ""
                        };
                    }
                });
            };

            //close record
            $scope.closeRecordModal = function () {
                $scope.recordModal = {
                    id: 0,
                    weight: "",
                    series: "",
                    repetitions: "",
                    commentary: ""
                };
                $("#recordModal").modal("hide");
            };
        }]);
