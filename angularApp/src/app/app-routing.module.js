"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
const core_1 = require('@angular/core');
const router_1 = require('@angular/router');
const groups_component_1 = require('./tabs/groups/groups.component');
const plugs_component_1 = require('./tabs/plugs/plugs.component');
const schedules_component_1 = require('./tabs/schedules/schedules.component');
const rules_component_1 = require('./tabs/rules/rules.component');
const sensors_component_1 = require('./tabs/sensors/sensors.component');
const scenes_component_1 = require('./tabs/scenes/scenes.component');
const lights_component_1 = require('./tabs/lights/lights.component');
const app_component_1 = require('./app.component');
const angular = 'angular/';
const routes = [
    {
        path: 'groups',
        component: groups_component_1.GroupsComponent
    },
    {
        path: 'lights',
        component: lights_component_1.LightsComponent
    },
    {
        path: 'plugs',
        component: plugs_component_1.PlugsComponent
    },
    {
        path: 'scenes',
        component: scenes_component_1.ScenesComponent
    },
    {
        path: 'schedules',
        component: schedules_component_1.SchedulesComponent
    },
    {
        path: 'sensors',
        component: sensors_component_1.SensorsComponent
    },
    {
        path: 'rules',
        component: rules_component_1.RulesComponent
    },
    {
        path: 'app',
        component: app_component_1.AppComponent
    },
    // {
    //   path: 'edit/:itemId',
    //   component: HeroDetailComponent
    // },
    {
        path: '',
        redirectTo: '/scenes',
        pathMatch: 'full'
    }
];
let AppRoutingModule = class AppRoutingModule {
};
AppRoutingModule = __decorate([
    core_1.NgModule({
        imports: [router_1.RouterModule.forRoot(routes)],
        exports: [router_1.RouterModule]
    }), 
    __metadata('design:paramtypes', [])
], AppRoutingModule);
exports.AppRoutingModule = AppRoutingModule;
