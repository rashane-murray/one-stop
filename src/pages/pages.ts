import { RecentPage } from './recent/recent';
import { MapPage } from './map/map';
import { SettingsPage } from './settings/settings';
import { TabsPage } from './tabs/tabs';


// The main page the user will see as they use the app over a long period of time.
// Change this if not using tabs
export const MainPage = TabsPage;

// The initial root pages for our tabs (remove if not using tabs)
export const Tab1Root = RecentPage;
export const Tab2Root = MapPage;
export const Tab3Root = SettingsPage;
