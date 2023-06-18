# Versions

### [v1.5.0](smarthome-v1.5.0.apk):
- Get the room state from the initial room response

### [v1.4.5](smarthome-v1.4.5.apk):
- Stop the rooms list from updating device states on every view update

### [v1.4.4](smarthome-v1.4.4.apk):
- Fix the state and light values (on/off, brightness, color, etc.) changing to the wrong values

### [v1.4.3](smarthome-v1.4.3.apk):
- Allow non-light devices to be included in rooms
    - 🤓 Make DeviceState.light_state optional

### [v1.4.2](smarthome-v1.4.2.apk):
- Fix bug

### [v1.4.1](smarthome-v1.4.1.apk):
- Fix toggle button "off" color in dark mode (was white, should be dark)

### [v1.4.0](smarthome-v1.4.0.apk):
- Add light color temperature/RGB color controls
- Change UI to make on/off buttons toggle and highlight on or off based on the light state

### [v1.3.1](smarthome-v1.3.1.apk):
- Fix crash when tapping on "All" room

### [v1.3.0](smarthome-v1.3.0.apk):
- Add light control (brightness, color temperature) per room

### [v1.2.1](smarthome-v1.2.1.apk):
- 🤓 Implement Smart Home API v2.0.0

### [v1.2.0](smarthome-v1.2.0.apk):
- Fix non-wrapping room names
- Change theme colors to match app icon
- Add custom scrolling toolbar
- Standardize icon

### [v1.1.1](smarthome-v1.1.1.apk):
- Slight stylistic change to rooms list
  - 🤓 Refactor rooms list layout to use LinearLayout

### [v1.1.0](smarthome-v1.1.0.apk):
- Add "All" lighting control option to turn all lights on/off
- 🤓 Remove hard-coded rooms and send requests to API to retrieve list of rooms

### [v1.0.0](smarthome-v1.0.0.apk):
- Add lighting control at the room level
