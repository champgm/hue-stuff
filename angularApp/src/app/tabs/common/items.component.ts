import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { JsonEditorModule } from 'ng2-json-editor';

import 'rxjs/add/operator/toPromise';

export abstract class ItemsComponent implements OnInit {
  items: any;
  itemIds: string[];
  selectedItemId: string;
  selectUri: string;
  itemsUri: string;
  toEditItemId: string;
  toViewItemId: string;
  JSON: JSON;
  itemJsonToEdit: string;
  itemToEdit: any;

  constructor(private http: Http) {
    this.JSON = JSON;
  }

  ngOnInit(): void {
    this.getItems();
  }

  httpGet(uri: string) {
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

  onSelect(itemId: string) {
    console.log(`${this.constructor.name}: onSelect called with itemId: ${itemId}`);
    this.selectedItemId = itemId;
    if (this.selectUri) {
      return this.httpGet(`${this.selectUri}${this.selectedItemId}`);
    }
  }

  onEdit(itemId: string) {
    console.log(`${this.constructor.name}: onEdit called with itemId: ${itemId}`);
    this.toEditItemId = itemId;
    this.toViewItemId = undefined;
    this.itemToEdit = this.items[itemId];
    this.itemJsonToEdit = this.prettyPrint(this.items[itemId]);
  }

  onView(itemId: string) {
    console.log(`${this.constructor.name}: onView called with itemId: ${itemId}`);
    this.toViewItemId = itemId;
    this.toEditItemId = undefined;
    this.itemJsonToEdit = undefined;
  }

  submitJson() {
    console.log(`${this.constructor.name}: submitJson called`);
    console.log(`${this.constructor.name}: JSON was: ${JSON.stringify(this.itemJsonToEdit)}`);
    this.itemJsonToEdit = "boop boop beep";
  }

  prettyPrint(jsonItem: any) {
    return JSON.stringify(jsonItem, null, 4);
  }

  abstract isOn(itemId: string): boolean;
}
