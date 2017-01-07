angular.module('trainingTrackerApp')

    // 'auth' service manage the authentication function of the page with the server
    .factory('auth', function ($state, $http, $base64) {

        var _identity = undefined,
            _authenticated = false;

        return {
            //return true if the user is authenticated
            isAuthenticated: function () {
                if (_authenticated) {
                    return _authenticated;
                } else {
                    var tmp = angular.fromJson(localStorage.userIdentity);
                    if (tmp !== undefined) {
                        this.authenticate(tmp);
                        return _authenticated;
                    } else {
                        return false;
                    }
                }
            },

            //authenticate the [identity] user
            authenticate: function (identity) {
                _identity = identity;
                _authenticated = identity !== undefined;
                localStorage.userIdentity = angular.toJson(_identity);
            },

            //logout function
            logout: function () {
                _identity = undefined;
                _authenticated = false;
                localStorage.removeItem('userIdentity');
                localStorage.removeItem('exerciseObject');
                $state.go('starter');
            },

            getUserObject: function () {
                return _identity;
            },

            getUsername: function () {
                return _identity.user;
            },

            getEmail: function () {
                return _identity.email;
            },

            //send the login info to the server
            login: function (user, password, callback) {
                var that = this;
                $http({
                    method: 'GET',
                    url: 'signIn',
                    headers: {
                        'Authorization': 'Basic ' +
                        $base64.encode(user + ":" + password)
                    }
                }).success(function (data) {
                    that.authenticate(data);
                    $state.go('home');

                }).error(function (data) {
                    callback(data);
                });
            },

            //send the register info to the server
            register: function (userObject, callback) {
                var that = this;
                $http({
                    method: 'POST',
                    url: 'signUp',
                    data: JSON.stringify(userObject),
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    that.authenticate(data);
                    $state.go('home');

                }).error(function (data) {
                    callback(data);
                });
            }
        };
    })

    // 'exerciseService' service manage the exercise home functions of the page with the server
    .factory('exerciseService', function ($state, $http, auth) {

        return {
            //get the exercises list
            getExercisesList: function (callbackSuccess,callbackError) {
                $http({
                    method: 'GET',
                    url: 'listPerformed',
                    headers: {
                        'user': auth.getUsername()
                    }
                }).success(function (data) {
                    callbackSuccess(data.listPerformed, data.listCardioPerformed);
                }).error(function (data) {
                    callbackError(data);
                });
            },
            // modify a custom exercise
            modifyExercise: function (exercise, callbackSuccess, callbackError, updateListExercise) {
                var exerciseTemp = {
                    user: auth.getUsername(),
                    id: exercise.id,
                    muscleGroup: exercise.muscleGroup,
                    name: exercise.name
                };
                $http({
                    method: 'POST',
                    url: 'modifyExercise',
                    data: JSON.stringify(exerciseTemp),
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    callbackSuccess('Ejercicio modificado correctamente');
                    updateListExercise(data.listPerformed, data.listCardioPerformed);
                }).error(function (data) {
                    callbackError(data);
                });
            },
            // delete a custom exercise
            deleteExercise: function (exercise, callbackSuccess, callbackError, updateListExercise) {
                var exerciseTemp = {
                    user: auth.getUsername(),
                    id: exercise.id
                };
                $http({
                    method: 'POST',
                    url: 'removeExercise',
                    data: JSON.stringify(exerciseTemp),
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    callbackSuccess('Ejercicio borrado correctamente');
                    updateListExercise(data.listPerformed, data.listCardioPerformed);
                }).error(function (data) {
                    callbackError(data);
                });
            },
            //get the predetermined exercises and predetermined muscle groups
            getPredetermined: function (callbackSuccess,callbackError) {
                $http({
                    method: 'GET',
                    url: 'getPredetermined'
                }).success(function (data) {
                    callbackSuccess(data.muscleGroups,data.predeterminedExercises, data.cardioTypes, data.cardioExercises);
                }).error(function (data) {
                    callbackError(data);
                });
            },
            // add a exercise to the list of performed exercises
            addExercise: function (exercise,callbackSuccess,callbackError) {
                var exerciseTemp = {
                    user: auth.getUsername(),
                    id: exercise.id,
                    name: exercise.name,
                    muscleGroup: exercise.muscleGroup
                };
                $http({
                    method: 'POST',
                    url: 'addExercise',
                    data: JSON.stringify(exerciseTemp),
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    callbackError(data);
                });
            },
            // add a cardiovascular exercise to the list of performed exercises
            addCardiovascular: function (exercise,callbackSuccess,callbackError) {
                var exerciseTemp = {
                    user: auth.getUsername(),
                    id: exercise.id,
                    cardioType: exercise.cardioType
                };
                $http({
                    method: 'POST',
                    url: 'addCardiovascular',
                    data: JSON.stringify(exerciseTemp),
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    callbackError(data);
                });
            }
        };
    })

    // 'recordsService' service manage the records of the exercises functions with the server
    .factory('recordsService', function ($state, $http, auth) {

        var exerciseObject = undefined;
        if ((exerciseObject == undefined) && (localStorage.exerciseObject !== undefined)) {
            exerciseObject = angular.fromJson(localStorage.exerciseObject);
        }

        return {
            // set the exercise name
            setExerciseObject: function (object) {
                localStorage.exerciseObject = angular.toJson(object);
                exerciseObject = object;
            },
            // get the exercise name
            getExerciseName: function () {
                return exerciseObject.name;
            },
            // get the exercise id
            getExerciseId: function () {
                return exerciseObject.id;
            },

            //get the exercises list
            getRecordHistoryList: function (numPage, callbackSuccess, callbackError) {
                $http({
                    method: 'GET',
                    url: 'listRecordHistory',
                    headers: {
                        'user': auth.getUsername(),
                        'numPage': numPage,
                        'id': this.getExerciseId()
                    }
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    callbackError(data);
                });
            },
            // save record of a exercise
            saveRecord: function (record,callbackSuccess,callbackError,updateListRecord) {
                var recordTemp = {
                    user: auth.getUsername(),
                    id: record.id,
                    weight: record.weight,
                    series: record.series,
                    repetitions: record.repetitions,
                    commentary: record.commentary
                };
                $http({
                    method: 'POST',
                    url: 'saveRecord',
                    data: JSON.stringify(recordTemp),
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    callbackSuccess('Marca añadida correctamente');
                    updateListRecord(data.listPerformed, data.listCardioPerformed);
                }).error(function (data) {
                    callbackError(data);
                });
            },
            // save record of a exercise
            saveCardio: function (record,callbackSuccess,callbackError,updateListRecord) {
                var recordTemp = {
                    user: auth.getUsername(),
                    id: record.id,
                    distance: record.distance,
                    time: record.time,
                    intensity: record.time
                };
                $http({
                    method: 'POST',
                    url: 'saveCardiovascularRecord',
                    data: JSON.stringify(recordTemp),
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    callbackSuccess('Marca añadida correctamente');
                    updateListRecord(data.listPerformed, data.listCardioPerformed);
                }).error(function (data) {
                    callbackError(data);
                });
            }
        };
    });
