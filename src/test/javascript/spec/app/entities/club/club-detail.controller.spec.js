'use strict';

describe('Controller Tests', function() {

    describe('Club Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockClub, MockTeam, MockClubEvent, MockUserExtraInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockClub = jasmine.createSpy('MockClub');
            MockTeam = jasmine.createSpy('MockTeam');
            MockClubEvent = jasmine.createSpy('MockClubEvent');
            MockUserExtraInfo = jasmine.createSpy('MockUserExtraInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Club': MockClub,
                'Team': MockTeam,
                'ClubEvent': MockClubEvent,
                'UserExtraInfo': MockUserExtraInfo
            };
            createController = function() {
                $injector.get('$controller')("ClubDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubplannerApp:clubUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
