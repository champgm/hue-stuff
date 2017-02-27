import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

@Component({
  moduleId: module.id,
  selector: 'plugs',
  templateUrl: '../common/items.component.html',
})
export class PlugsComponent implements OnInit {
  items: any;
  itemIds: string[];
  itemsUri: string = `/getplugs`;

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
    console.log(`${this.constructor.name}: Selected: ${itemId}`);
  }

  onEdit(itemId: string) {
    console.log(`${this.constructor.name}: Edited: ${itemId}`);
  }
}
