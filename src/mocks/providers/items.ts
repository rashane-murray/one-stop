import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { Item } from '../../models/item';

@Injectable()
export class Items {
  items: Item[] = [];

  defaultItem: any = {
    "name": "Burt Tomas",
    "profilePic": "assets/img/speakers/bear.jpg",
    "about": "estimated time of arrival",
    "location": {lat: 37.768, lng: -122.511}
  };


  constructor(public http: Http) {
    let items = [
      {
        "name": "Nelson - 32 Ridgeway dr",
        "profilePic": "assets/img/speakers/bear.jpg",
        "about": "3 mins away",
        "location": {lat: 30.768, lng: -170.511}
      },
      {
        "name": "Charlie - 16 Birdsucker Lane ",
        "profilePic": "assets/img/speakers/cheetah.jpg",
        "about": "10 mins away",
        "location": {lat: 35.998, lng: -169.711}
      },
      {
        "name": "Donald -23 Macjix ave",
        "profilePic": "assets/img/speakers/duck.jpg",
        "about": "17 mins away",
        "location": {lat: 39.768, lng: -162.511}
      },
      {
        "name": "Eva - 553 Halfway Tree rd",
        "profilePic": "assets/img/speakers/eagle.jpg",
        "about": "58 mins away",
        "location": {lat: 33.768, lng: -166.511}
      },
      {
        "name": "Ellie - Portmore Mall",
        "profilePic": "assets/img/speakers/elephant.jpg",
        "about": "79 away",
        "location": {lat: 35.648, lng: -161.671}
      },
      {
        "name": "Molly - Highway 53",
        "profilePic": "assets/img/speakers/mouse.jpg",
        "about": "Away",
        "location": {lat: 35.228, lng: -122.451}
      },
      {
        "name": "Paul - 21 higbury dr",
        "profilePic": "assets/img/speakers/puppy.jpg",
        "about": "Offline",
        "location": {lat: 31.738, lng: -152.453}
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
