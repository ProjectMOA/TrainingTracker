angular.module('trainingTrackerApp')

    .controller('homeCtrl', ['$scope', '$state', 'exerciseService', 'recordsService',
        function ($scope, $state, exerciseService, recordsService) {

            $scope.exercisesList = []; //exercise list
            $scope.cardioList = []; //cardio list
            // exercise list server request
            $scope.getExercisesList = function () {
                exerciseService.getExercisesList(function (exercises, cardio) {
                    $scope.exercisesList = exercises;
                    $scope.cardioList = cardio;
                },showError);
            };

            // INPUTS CONTROL LENGTH

            $scope.weightMaxLength = false;
            $scope.seriesMaxLength = false;
            $scope.repetitionsMaxLength = false;
            $scope.commentaryMaxLength = false;
            $scope.nameMaxLength = false;

            // Watches to control input variables length
            $scope.$watch('recordModal.weight', function () {
                if ($scope.recordModal.weight != undefined) {
                    if ($scope.recordModal.weight.length > 7) {
                        $scope.recordModal.weight = $scope.recordModal.weight.slice(0, 7);
                    } else if ($scope.recordModal.weight.length == 7) {
                        $scope.weightMaxLength = true;
                    } else {
                        $scope.weightMaxLength = false;
                    }
                } else {
                    $scope.weightMaxLength = false;
                }
            });
            $scope.$watch('recordModal.series', function () {
                if ($scope.recordModal.series != undefined) {
                    if ($scope.recordModal.series.length > 4) {
                        $scope.recordModal.series = $scope.recordModal.series.slice(0, 4);
                    } else if ($scope.recordModal.series.length == 4) {
                        $scope.seriesMaxLength = true;
                    } else {
                        $scope.seriesMaxLength = false;
                    }
                } else {
                    $scope.seriesMaxLength = false;
                }
            });
            $scope.$watch('recordModal.repetitions', function () {
                if ($scope.recordModal.repetitions != undefined) {
                    if ($scope.recordModal.repetitions.length > 4) {
                        $scope.recordModal.repetitions = $scope.recordModal.repetitions.slice(0, 4);
                    } else if ($scope.recordModal.repetitions.length == 4) {
                        $scope.repetitionsMaxLength = true;
                    } else {
                        $scope.repetitionsMaxLength = false;
                    }
                } else {
                    $scope.repetitionsMaxLength = false;
                }
            });
            $scope.$watch('recordModal.commentary', function () {
                if ($scope.recordModal.repetitions != undefined) {
                    if ($scope.recordModal.commentary.length > 250) {
                        $scope.recordModal.commentary = $scope.recordModal.commentary.slice(0, 250);
                    } else if ($scope.recordModal.commentary.length == 250) {
                        $scope.commentaryMaxLength = true;
                    } else {
                        $scope.commentaryMaxLength = false;
                    }
                } else {
                    $scope.commentaryMaxLength = false;
                }
            });
            $scope.$watch('modifyModal.name', function () {
                if ($scope.modifyModal.name != undefined) {
                    if ($scope.modifyModal.name.length > 50) {
                        $scope.modifyModal.name = $scope.modifyModal.name.slice(0, 50);
                    } else if ($scope.modifyModal.name.length == 50) {
                        $scope.nameMaxLength = true;
                    } else {
                        $scope.nameMaxLength = false;
                    }
                } else {
                    $scope.nameMaxLength = false;
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

            // MODAL RECORD SECTION

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
                        recordsService.saveRecord($scope.recordModal, showSuccess, showError,
                            function (exercises, cardio) {
                                $scope.exercisesList = exercises;
                                $scope.cardioList = cardio;
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

            // MODAL MODIFY SECTION

            $scope.modifyModal = {    // temporal modify data on modals
                id: 0,
                name: "",
                muscleGroup: ""
            };

            // open modal with [modify] information
            $scope.openModifyModal = function (exercise) {
                $("#modifyModal").modal("show");
                $scope.modifyModal = {
                    id: exercise.id,
                    name: exercise.name,
                    muscleGroup: exercise.muscleGroup
                };
            };

            // modify exercise
            $scope.modifyExerciseModal = function () {
                $("#modifyModal").modal("hide");
                $scope.saveModify = true; //flag to indicate that we are saving the exercise
                $("#modifyModal").on('hidden.bs.modal', function () {
                    if ($scope.saveModify) {
                        exerciseService.modifyExercise($scope.modifyModal, showSuccess, showError,
                            function (exercises, cardio) {
                                $scope.exercisesList = exercises;
                                $scope.cardioList = cardio;
                            });
                        $scope.saveModify = false;
                        $scope.modifyModal = {
                            id: 0,
                            name: "",
                            muscleGroup: ""
                        };
                    }
                });
            };

            //close modify
            $scope.closeModifyModal = function () {
                $scope.modifyModal = {
                    id: 0,
                    name: "",
                    muscleGroup: ""
                };
                $("#modifyModal").modal("hide");
            };

            // MODAL DELETE SECTION

            $scope.deleteModal = {    // temporal delete data on modals
                id: 0,
                name: "",
                muscleGroup: ""
            };

            // open modal with [delete] information
            $scope.openDeleteModal = function (exercise) {
                $("#deleteModal").modal("show");
                $scope.deleteModal = {
                    id: exercise.id,
                    name: exercise.name,
                    muscleGroup: exercise.muscleGroup
                };
            };

            //delete FORM modal
            $scope.deleteFormModal = function () {
                if ($scope.deleteExercise) {
                    $scope.deleteExerciseModal();
                } else {
                    $scope.closeDeleteModal();
                }
            };

            // delete exercise
            $scope.deleteExerciseModal = function () {
                $("#deleteModal").modal("hide");
                $scope.saveDelete = true; //flag to indicate that we are deleting the exercise
                $("#deleteModal").on('hidden.bs.modal', function () {
                    if ($scope.saveDelete) {
                        exerciseService.deleteExercise($scope.deleteModal, showSuccess, showError,
                            function (exercises, cardio) {
                                $scope.exercisesList = exercises;
                                $scope.cardioList = cardio;
                            });
                        $scope.saveDelete = false;
                        $scope.deleteModal = {
                            id: 0,
                            name: "",
                            muscleGroup: ""
                        };
                    }
                });
            };

            //close delete
            $scope.closeDeleteModal = function () {
                $scope.deleteModal = {
                    id: 0,
                    name: "",
                    muscleGroup: ""
                };
                $("#deleteModal").modal("hide");
            };

            // MODAL CARDIO SECTION

            $scope.cardioModal = {    // temporal record data on modals
                id: 0,
                distance: "",
                time: "",
                intensity: ""
            };

            // open modal with [record] information
            $scope.openCardioModal = function (cardio) {
                $("#cardioModal").modal("show");
                $scope.cardioModal = {
                    id: cardio.id,
                    distance: "",
                    time: "",
                    intensity: ""
                };
            };

            // save cardio
            $scope.saveCardio = function () {
                $("#cardioModal").modal("hide");
                $scope.saveCardioRecord = true; //flag to indicate that we are saving the record
                $("#cardioModal").on('hidden.bs.modal', function () {
                    if ($scope.saveCardioRecord) {
                        recordsService.saveCardio($scope.cardioModal, showSuccess, showError,
                            function (exercises, cardio) {
                                $scope.exercisesList = exercises;
                                $scope.cardioList = cardio;
                            });
                        $scope.saveCardioRecord = false;
                        $scope.cardioModal = {
                            id: 0,
                            distance: "",
                            time: "",
                            intensity: ""
                        };
                    }
                });
            };

            //close cardio
            $scope.closeCardioModal = function () {
                $scope.cardioModal = {
                    id: 0,
                    distance: "",
                    time: "",
                    intensity: ""
                };
                $("#cardioModal").modal("hide");
            };

        }]);
