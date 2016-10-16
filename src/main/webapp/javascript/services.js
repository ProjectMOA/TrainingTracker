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
                        base64.encode(user + ":" + password)
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
    });
