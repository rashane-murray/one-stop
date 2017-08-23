import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule, Http } from '@angular/http';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { Storage, IonicStorageModule } from '@ionic/storage';

import { MyApp } from './app.component';

import { BookingsPage } from '../pages/bookings/bookings';
import { ContentPage } from '../pages/content/content';
import { ItemCreatePage } from '../pages/item-create/item-create';
import { TripPage } from '../pages/trip-detail/trip-detail';
import { RecentPage } from '../pages/recent/recent';
import { LoginPage } from '../pages/login/login';
import { MapPage } from '../pages/map/map';
import { MenuPage } from '../pages/menu/menu';
import { SearchPage } from '../pages/search/search';
import { SettingsPage } from '../pages/settings/settings';
import { SignupPage } from '../pages/signup/signup';
import { TabsPage } from '../pages/tabs/tabs';
import { WelcomePage } from '../pages/welcome/welcome';
import { LocationSelect } from '../pages/location-select/location-select';
import { AngularFireModule } from "angularfire2";
import { AngularFireAuthModule } from "angularfire2/auth";
import { AngularFireDatabase } from "angularfire2/database";
import { FirebaseListObservable } from "angularfire2/database";

import { Api } from '../providers/api';
import { Items } from '../mocks/providers/items';
import { Settings } from '../providers/settings';
import { User } from '../providers/user';

import { Camera } from '@ionic-native/camera';
import { Geolocation } from '@ionic-native/geolocation';
import { GoogleMaps } from '@ionic-native/google-maps';
import { Network } from '@ionic-native/network';
import { Connectivity } from '../providers/connectivity-service/connectivity-service';
import { GoogleMapsProvider } from '../providers/google-maps/google-maps';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';
import { DriverLocationProvider } from '../providers/driver-location/driver-location';
//import { GoogleAuth, User } from '@ionic/cloud-angular';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { FIREBASE_CONFIG } from "./app.firebase.config";
// The translate loader needs to know where to load i18n files
// in Ionic's static asset pipeline.

export function HttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

export function provideSettings(storage: Storage) {
  /**
  *   The Settings provider takes a set of default settings for your app.
  *   You can add new settings options at any time. Once the settings are saved,
  *   these values will not overwrite the saved values (this can be done manually if
  *   desired).
  */
  return new Settings(storage, {
    option1: true,
    option2: 'ONE STOP',
    option3: '3',
    option4: 'Hello'
  });
}


/*
  The Pages array lists all of the pages we want to use in our app.
  We then take these pages and inject them into our NgModule so Angular
  can find them. As you add and remove pages, make sure to keep this list up to date.
 */
let pages = [
  MyApp,
  BookingsPage,
  ContentPage,
  ItemCreatePage,
  TripPage,
  RecentPage,
  LoginPage,
  MapPage,
  MenuPage,
  SearchPage,
  SettingsPage,
  SignupPage,
  TabsPage,
  WelcomePage,
  LocationSelect
];

export function declarations() {
  return pages;
}

export function entryComponents() {
  return pages;
}

export function providers() {
  return [
    Api,
    Items,
    User,
    Camera,
    Connectivity,
    GoogleMapsProvider,
    Network,
    Geolocation,
    GoogleMaps,
    SplashScreen,
    StatusBar,
    DriverLocationProvider, 
    AngularFireDatabase,

    { provide: Settings, useFactory: provideSettings, deps: [Storage] },
    
    // Keep this to enable Ionic's runtime error handling during development
    { provide: ErrorHandler, useClass: IonicErrorHandler }
  ];
}

@NgModule({
  declarations: declarations(),
  imports: [
    BrowserModule,
    HttpModule,
    AngularFireModule.initializeApp(FIREBASE_CONFIG),
    AngularFireAuthModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [Http]
      }
    }),
    IonicModule.forRoot(MyApp),
    IonicStorageModule.forRoot()
  ],
  bootstrap: [IonicApp],
  entryComponents: entryComponents(),
  providers: providers()
})
export class AppModule { }
