import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Http } from '@angular/http';
import {ItemsComponent} from '../common/items.component';

import 'rxjs/add/operator/toPromise';

@Component({
  moduleId: module.id,
  selector: 'plugs',
  templateUrl: '../common/items.component.html',
})
export class PlugsComponent extends ItemsComponent implements OnInit {
  itemsUri: string = `/getplugs`;

  constructor(http: Http) {
    super(http);
  }

}
