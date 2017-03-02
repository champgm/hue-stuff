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
const http_1 = require('@angular/http');
const items_component_1 = require('../common/items.component');
require('rxjs/add/operator/toPromise');
let GroupsComponent = class GroupsComponent extends items_component_1.ItemsComponent {
    constructor(http) {
        super(http);
        this.itemsUri = `/getgroups`;
    }
    ngOnInit() {
        this.getItems();
    }
};
GroupsComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'groups',
        templateUrl: '../common/items.component.html',
    }), 
    __metadata('design:paramtypes', [http_1.Http])
], GroupsComponent);
exports.GroupsComponent = GroupsComponent;
