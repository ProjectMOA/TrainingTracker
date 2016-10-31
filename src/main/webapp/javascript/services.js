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
                _authenticated = identity != null && identity != undefined;
                localStorage.userIdentity = angular.toJson(_identity);
            },

            //logout function
            logout: function () {
                auth.authenticate(null);
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
            getExercisesList: function (callbackSuccess,CallbackError) {
                $http({
                    method: 'GET',
                    url: 'listPerformed',
                    headers: {
                        'user': auth.getUsername()
                    }
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    CallbackError(data);
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
                    CallbackError(data);
                });
            }
        };
    });
