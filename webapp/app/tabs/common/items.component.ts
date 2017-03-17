import { Component, ViewChild } from '@angular/core';
import { OnInit } from '@angular/core';
import { Http } from '@angular/http';
// import { ModalDirective } from 'ng2-bootstrap/modal';
import { ModalDirective } from 'ng2-bootstrap';

import 'rxjs/add/operator/toPromise';

export abstract class ItemsComponent implements OnInit {
  http: Http;
  JSON: JSON;

  @ViewChild('viewModal') public viewModal: ModalDirective;
  @ViewChild('editModal') public editModal: ModalDirective;
  @ViewChild('editResultModal') public editResultModal: ModalDirective;

  topPad: number = 50;
  updateUri: string;
  selectUri: string;
  itemsUri: string;
  items: any;
  itemIds: string[];
  selectedItemId: string;
  itemIdToView: string;
  itemIdToEdit: string;
  itemToEdit: any;
  itemJsonToEdit: string;
  editResult: string;

  constructor(http: Http) {
    this.http = http;
    this.JSON = JSON;
  }

  ngOnInit(): void {
    this.getItems();
  }

  httpGet(uri: string) {
    return this.http.get(uri).toPromise();
  }

  async getItems() {
    await this.httpGet(this.itemsUri)
      .then(response => {
        const json = response.json();
        this.itemIds = Object.keys(json);
        this.items = json;
      });
  }

  onSelect(itemId: string) {
    console.log(`${this.constructor.name}: onSelect called with itemId: ${itemId}`);
    this.selectedItemId = itemId;
    if (this.selectUri) {
      return this.httpGet(`${this.selectUri}${this.selectedItemId}`);
    }
  }

  onView(itemId: string) {
    console.log(`${this.constructor.name}: onView called with itemId: ${itemId}`);
    this.itemIdToView = itemId;
    this.itemIdToEdit = undefined;
    this.itemJsonToEdit = undefined;
    this.viewModal.show();
  }

  clearView() {
    this.viewModal.hide();
    this.itemIdToView = undefined;
  }

  onEdit(itemId: string) {
    console.log(`${this.constructor.name}: onEdit called with itemId: ${itemId}`);
    this.itemIdToEdit = itemId;
    this.itemIdToView = undefined;
    this.itemToEdit = this.items[itemId];
    this.itemJsonToEdit = this.prettyPrint(this.items[itemId]);
    this.editModal.show();
  }

  async submitJson() {
    let editedItem;
    try {
      editedItem = JSON.parse(this.itemJsonToEdit);
    } catch (error) {
      this.editResult = `Unable to parse input JSON: ${this.itemJsonToEdit}`;
      return;
    }
    if (this.updateUri) {
      const response = await this.http.put(`${this.updateUri}${this.itemIdToEdit}`, editedItem).toPromise();
      this.editResult = JSON.parse(response["_body"]);
    }
    this.editResultModal.show();
    await this.getItems();
    this.onEdit(this.itemIdToEdit);
  }

  resetEdit() {
    this.itemJsonToEdit = this.prettyPrint(this.items[this.itemIdToEdit]);
  }

  cancelEdit() {
    this.itemJsonToEdit = undefined;
    this.itemIdToEdit = undefined;
    this.itemToEdit = undefined;
    this.editModal.hide();
  }

  clearEditResult() {
    this.editResult = undefined;
    this.editResultModal.hide();
  }

  prettyPrint(jsonItem: any) {
    return JSON.stringify(jsonItem, null, 4);
  }

  isOn(itemId: string): boolean {
    return true;
  }
}
