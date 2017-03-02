import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Http } from '@angular/http';
import {ItemsComponent} from '../common/items.component';

import 'rxjs/add/operator/toPromise';

@Component({
  moduleId: module.id,
  selector: 'lights',
  templateUrl: '../common/items.component.html',
})
export class LightsComponent extends ItemsComponent implements OnInit {
  itemsUri: string = `/getlights`;

  constructor(http: Http) {
    super(http);
  }

}
