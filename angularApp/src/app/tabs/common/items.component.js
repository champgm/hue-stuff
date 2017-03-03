"use strict";
require('rxjs/add/operator/toPromise');
class ItemsComponent {
    constructor(http) {
        this.http = http;
        this.JSON = JSON;
    }
    ngOnInit() {
        this.getItems();
    }
    httpGet(uri) {
        return this.http.get(uri).toPromise();
    }
    getItems() {
        this.httpGet(this.itemsUri)
            .then(response => {
            const json = response.json();
            this.itemIds = Object.keys(json);
            this.items = json;
        });
    }
    onSelect(itemId) {
        console.log(`${this.constructor.name}: onSelect called with itemId: ${itemId}`);
        this.selectedItemId = itemId;
        if (this.selectUri) {
            return this.httpGet(`${this.selectUri}${this.selectedItemId}`);
        }
    }
    onEdit(itemId) {
        console.log(`${this.constructor.name}: onEdit called with itemId: ${itemId}`);
        this.toEditItemId = itemId;
        this.toViewItemId = undefined;
        this.itemToEdit = this.items[itemId];
        this.itemJsonToEdit = this.prettyPrint(this.items[itemId]);
    }
    onView(itemId) {
        console.log(`${this.constructor.name}: onView called with itemId: ${itemId}`);
        this.toViewItemId = itemId;
        this.toEditItemId = undefined;
        this.itemJsonToEdit = undefined;
    }
    // getFullItem()
    submitJson(itemId) {
        console.log(`${this.constructor.name}: submitJson called`);
        console.log(`${this.constructor.name}: JSON was: ${this.itemJsonToEdit}`);
        this.itemJsonToEdit = "boop boop beep";
    }
    prettyPrint(jsonItem) {
        return JSON.stringify(jsonItem, null, 4);
    }
}
exports.ItemsComponent = ItemsComponent;
