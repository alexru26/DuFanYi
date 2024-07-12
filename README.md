# 读翻译 (DuFanYi)
Yes, the app name literally means "read translate." This app was heavily inspired by [TachiyomiSY](https://github.com/jobobby04/TachiyomiSY), a fantastic app to read manga. Thanks jobobby04 👍.

## App Architecture
- Library displays Room database
- Clicking a Series opens a page that displays basic information
  - Information includes chapter listing
  - Clicking chapter opens reader
- Browse has button that lets user choose txt and adds new series into library (temporary)
  - txt file first three lines should be name, author, status
  - txt file chapters should be "第X章 ..." or just "第X章"
  - example: [他先爱上她的_毛吉.txt](https://github.com/user-attachments/files/16183211/_.txt)
- Settings does nothing right now
