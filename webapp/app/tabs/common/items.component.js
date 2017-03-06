"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
require("rxjs/add/operator/toPromise");
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
        return __awaiter(this, void 0, void 0, function* () {
            yield this.httpGet(this.itemsUri)
                .then(response => {
                const json = response.json();
                this.itemIds = Object.keys(json);
                this.items = json;
            });
        });
    }
    onSelect(itemId) {
        console.log(`${this.constructor.name}: onSelect called with itemId: ${itemId}`);
        this.selectedItemId = itemId;
        if (this.selectUri) {
            return this.httpGet(`${this.selectUri}${this.selectedItemId}`);
        }
    }
    onView(itemId) {
        console.log(`${this.constructor.name}: onView called with itemId: ${itemId}`);
        this.itemIdToView = itemId;
        this.itemIdToEdit = undefined;
        this.itemJsonToEdit = undefined;
    }
    clearView() {
        this.itemIdToView = undefined;
    }
    onEdit(itemId) {
        console.log(`${this.constructor.name}: onEdit called with itemId: ${itemId}`);
        this.itemIdToEdit = itemId;
        this.itemIdToView = undefined;
        this.itemToEdit = this.items[itemId];
        this.itemJsonToEdit = this.prettyPrint(this.items[itemId]);
    }
    submitJson() {
        return __awaiter(this, void 0, void 0, function* () {
            let editedItem;
            try {
                editedItem = JSON.parse(this.itemJsonToEdit);
            }
            catch (error) {
                this.editResult = `Unable to parse input JSON: ${this.itemJsonToEdit}`;
                return;
            }
            if (this.updateUri) {
                const response = yield this.http.put(`${this.updateUri}${this.itemIdToEdit}`, editedItem).toPromise();
                this.editResult = JSON.parse(response["_body"]);
            }
            yield this.getItems();
            this.onEdit(this.itemIdToEdit);
        });
    }
    resetEdit() {
        this.itemJsonToEdit = this.prettyPrint(this.items[this.itemIdToEdit]);
    }
    cancelEdit() {
        this.itemJsonToEdit = undefined;
        this.itemIdToEdit = undefined;
        this.itemToEdit = undefined;
    }
    clearEditResult() {
        this.editResult = undefined;
    }
    prettyPrint(jsonItem) {
        return JSON.stringify(jsonItem, null, 4);
    }
}
exports.ItemsComponent = ItemsComponent;
