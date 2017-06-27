import { Component,ViewChild } from '@angular/core';
import { NavController } from 'ionic-angular';
//import { TranslateService } from '@ngx-translate/core';

import { Tab1Root } from '../pages';
import { Tab2Root } from '../pages';
import { Tab3Root } from '../pages';

@Component({
  selector: 'page-tabs',
  templateUrl: 'tabs.html'
})
export class TabsPage {


  @ViewChild('myTabs') tabRef;
  nyTabs:any;

  
  tab1Root: any = Tab1Root;
  tab2Root: any = Tab2Root;
  tab3Root: any = Tab3Root;

  tab1Title = " ";
  tab2Title = " ";
  tab3Title = " ";

  constructor(public navCtrl: NavController){
    this.tab1Title="Recent";
    this.tab2Title="Search";
    this.tab3Title="Settings";
    
    //this.tabRef.select(2);
  }
}
