import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

export class ItemsComponent implements OnInit {
  items: any;
  itemIds: string[];
  selectedItemId: string;
  itemsUri: string;

  constructor(private http: Http) { }

  ngOnInit(): void {
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

  onSelect(itemId: string) {
    console.log(`${this.constructor.name}: onSelect called with itemId: ${itemId}`);
    this.selectedItemId = itemId;
  }

  onEdit(itemId: string) {
    console.log(`${this.constructor.name}: onEdit called with itemId: ${itemId}`);
  }
}
