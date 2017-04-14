import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { ItemsComponent } from '../common/items.component';

import 'rxjs/add/operator/toPromise';

@Component({
  // moduleId: module.id,
  selector: 'groups',
  templateUrl: '../common/items.component.html',
})
export class GroupsComponent extends ItemsComponent implements OnInit {
  itemType: string = 'groups';

  constructor(http: Http) {
    super(http);
  }
}
