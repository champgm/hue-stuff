"use strict";
require('rxjs/add/operator/toPromise');
// @Component({
//   moduleId: module.id,
//   selector: 'items',
//   templateUrl: './items.component.html',
// })
class ItemsComponent {
    constructor(http) {
        this.http = http;
    }
    ngOnInit() {
        this.getItems();
    }
    getItems() {
        this.http.get(this.itemsUri).toPromise()
            .then(response => {
            const json = response.json();
            this.itemIds = Object.keys(json);
            this.items = json;
        });
    }
    onSelect(itemId) {
        console.log(`${this.constructor.name}: onSelect called with itemId: ${itemId}`);
        this.selectedItemId = itemId;
    }
    onEdit(itemId) {
        console.log(`${this.constructor.name}: onEdit called with itemId: ${itemId}`);
    }
}
exports.ItemsComponent = ItemsComponent;
