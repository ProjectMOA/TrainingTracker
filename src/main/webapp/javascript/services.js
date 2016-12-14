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
                localStorage.removeItem('exerciseName');
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
                    callbackSuccess(data);
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
                    updateListExercise(data);
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
                    updateListExercise(data);
                }).error(function (data) {
                    callbackError(data);
                });
            },
            //get the predetermined exercises and predetermined muscle groups
            getPredetermined: function (callbackSuccess,callbackError) {
                $http({
                    method: 'GET',
                    url: 'getPredetermined',
                    headers: {
                        'user': auth.getUsername()
                    }
                }).success(function (data) {
                    callbackSuccess(data.muscleGroups,data.predeterminedExercises);
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
            }
        };
    })

    // 'recordsService' service manage the records of the exercises functions with the server
    .factory('recordsService', function ($state, $http, auth) {

        var exerciseName = undefined;
        if ((exerciseName == undefined) && (localStorage.exerciseName !== undefined)) {
            exerciseName = angular.fromJson(localStorage.exerciseName);
        }

        return {
            // set the exercise name
            setExerciseName: function (name) {
                localStorage.exerciseName = angular.toJson(name);
                exerciseName = name;
            },

            // get the exercise name
            getExerciseName: function () {
                return exerciseName;
            },

            //get the exercises list
            getRecordHistoryList: function (numPage, callbackSuccess, callbackError) {
                $http({
                    method: 'GET',
                    url: 'listRecordHistory',
                    headers: {
                        'user': auth.getUsername(),
                        'numPage': numPage
                    }
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    var record1 = [
                        {weight: 1, series: 1, repetitions: 1, commentary: "blablabla", date: "1"},
                        {weight: 2, series: 2, repetitions: 2, commentary: "blublublu", date: "1"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "1"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "1"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "1"}
                    ];
                    var record2 = [
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "2"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "2"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "2"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "2"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "2"}
                    ];
                    var record3 = [
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "3"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "3"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "3"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "3"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "3"}
                    ];
                    var record4 = [
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "4"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "4"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "4"},
                        {weight: 3, series: 3, repetitions: 3, commentary: "bliblibli", date: "4"},
                        {weight: 4, series: 4, repetitions: 4, commentary: "blebleble", date: "4"}
                    ];
                    switch (numPage) {
                        case 1:
                            callbackSuccess(record1);
                            break;
                        case 2:
                            callbackSuccess(record2);
                            break;
                        case 3:
                            callbackSuccess(record3);
                            break;
                        default:
                            callbackSuccess(record4);
                    }
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
                    updateListRecord(data);
                }).error(function (data) {
                    callbackError(data);
                });
            }
        };
    });
