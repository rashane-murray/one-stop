import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { Item } from '../../models/item';

@Injectable()
export class Items {
  items: Item[] = [];

  defaultItem: any = {
    "name": "Burt Bear",
    "profilePic": "assets/img/speakers/bear.jpg",
    "about": "Burt is a Bear.",
  };


  constructor(public http: Http) {
    let items = [
      {
        "name": "Burt ",
        "profilePic": "assets/img/speakers/bear.jpg",
        "about": "Online"
      },
      {
        "name": "Charlie ",
        "profilePic": "assets/img/speakers/cheetah.jpg",
        "about": "Online"
      },
      {
        "name": "Donald ",
        "profilePic": "assets/img/speakers/duck.jpg",
        "about": "Offline"
      },
      {
        "name": "Eva ",
        "profilePic": "assets/img/speakers/eagle.jpg",
        "about": "Offline"
      },
      {
        "name": "Ellie",
        "profilePic": "assets/img/speakers/elephant.jpg",
        "about": "Online"
      },
      {
        "name": "Molly ",
        "profilePic": "assets/img/speakers/mouse.jpg",
        "about": "Away"
      },
      {
        "name": "Paul ",
        "profilePic": "assets/img/speakers/puppy.jpg",
        "about": "Away"
      }
    ];

    for (let item of items) {
      this.items.push(new Item(item));
    }
  }

  query(params?: any) {
    if (!params) {
      return this.items;
    }

    return this.items.filter((item) => {
      for (let key in params) {
        let field = item[key];
        if (typeof field == 'string' && field.toLowerCase().indexOf(params[key].toLowerCase()) >= 0) {
          return item;
        } else if (field == params[key]) {
          return item;
        }
      }
      return null;
    });
  }

  add(item: Item) {
    this.items.push(item);
  }

  delete(item: Item) {
    this.items.splice(this.items.indexOf(item), 1);
  }
}
