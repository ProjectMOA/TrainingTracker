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

            // INPUTS CONTROL LENGTH

            $scope.weightMaxLength = false;
            $scope.seriesMaxLength = false;
            $scope.repetitionsMaxLength = false;
            $scope.commentaryMaxLength = false;

            // Watches to control input variables length
            $scope.$watch('recordModal.weight', function () {
                if ($scope.recordModal.weight.length > 7) {
                    $scope.recordModal.weight = $scope.recordModal.weight.slice(0, 7);
                } else if ($scope.recordModal.weight.length == 7) {
                    $scope.weightMaxLength = true;
                } else {
                    $scope.weightMaxLength = false;
                }
            });
            $scope.$watch('recordModal.series', function () {
                if ($scope.recordModal.series.length > 4) {
                    $scope.recordModal.series = $scope.recordModal.series.slice(0, 4);
                } else if ($scope.recordModal.series.length == 4) {
                    $scope.seriesMaxLength = true;
                } else {
                    $scope.seriesMaxLength = false;
                }
            });
            $scope.$watch('recordModal.repetitions', function () {
                if ($scope.recordModal.repetitions.length > 4) {
                    $scope.recordModal.repetitions = $scope.recordModal.repetitions.slice(0, 4);
                } else if ($scope.recordModal.repetitions.length == 4) {
                    $scope.repetitionsMaxLength = true;
                } else {
                    $scope.repetitionsMaxLength = false;
                }
            });
            $scope.$watch('recordModal.commentary', function () {
                if ($scope.recordModal.commentary.length > 250) {
                    $scope.recordModal.commentary = $scope.recordModal.commentary.slice(0, 250);
                } else if ($scope.recordModal.commentary.length == 250) {
                    $scope.commentaryMaxLength = true;
                } else {
                    $scope.commentaryMaxLength = false;
                }
            });

            // FEEDBACK MESSAGES

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

            $scope.getExercisesList();

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
                debugger;
                $scope.recordModal = {
                    id: exercise.id,
                    weight: "",
                    series: "",
                    repetitions: "",
                    commentary: ""
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
