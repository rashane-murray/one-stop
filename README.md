# ONE-STOP 

<img src="src/assets/img/taxi.png" width="200" />

## Description

One-stop app seeks to prives Jamaicans with a simple and safe way to 'Hail a cab' and get to a desired destination.

## Table of contents

1. [Installation](#installation)
2. [Usage](#usage)
3. [Pages](#pages)
4. [Providers](#providers)
5. [i18n](#i18n) (adding languages)

## Installation

To run this app, install the latest version of the Ionic CLI and clone the repo:

```bash
git clone https://github.com/PalisadoesFoundation/one-stop.git
```

```bash
git cd one-stop
```

then run (in web browser):


```bash
npm run ionic:serve --lab
```

## Pages

The app loads with the `WelcomePage` where the user will register or login. Once the user is authenticated, the app will load with the `MainPage` which is set to be the `TabsPage` as the default, displaying recent visits, search and settings.

The entry and main pages can be configured easily by updating the corresponding variables in `src/pages/pages.ts`

Please read the readme for each page in the source for more documentation on each.

## Usage

You can easily navigate through the apps pages to test its functionality and user experience by tapping on respective locations and input from your ios/android/windows keyboard.

## Providers

One-Stop comes with some basic implementations of common providers:

### User

The `User` provider is used to authenticate users through its `login(accountInfo)` and `signup(accountInfo)` methods, which perform `POST` requests to an API endpoint that you will need to configure.

### Api

The `Api` provider is a simple CRUD frontend to an API. Simply put the root of your API url in the Api class and call get/post/put/patch/delete 

## i18n

Ionic Super Starter comes with internationalization (i18n) out of the box with [ngx-translate](https://github.com/ngx-translate/core). This makes it easy to change the text used in the app by modifying only one file. 

By default, the only language strings provided are American English.

### Adding Languages

To add new languages, add new files to the `src/assets/i18n` directory, following the pattern of LANGCODE.json where LANGCODE is the language/locale code (ex: en/gb/de/es/etc.).

### Changing the Language

To change the language of the app, edit `src/app/app.component.ts` and modify `translate.use('en')` to use the LANGCODE from `src/assets/i18n/`
