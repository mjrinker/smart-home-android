## Beta build checklist:

- append `"-beta"` to `versionName` in build.gradle (:app)
- append `".beta"` to `applicationId` in build.gradle (:app)
- append ` (β)` to `@string\app_name`
- uncomment lines 37-45 in `ic_launcher_foreground.xml`
- change the following in `colors.xml`:

```diff
-    <color name="colorPrimary">@color/colorPrimary</color>
-    <color name="colorPrimaryDark">@color/colorPrimaryDark</color>
-    <color name="colorAccent">@color/colorAccent</color>
+    <color name="colorPrimary">@color/colorPrimaryBeta</color>
+    <color name="colorPrimaryDark">@color/colorPrimaryDarkBeta</color>
+    <color name="colorAccent">@color/colorAccentBeta</color>
```

- Create new Vector Asset for the app icon
