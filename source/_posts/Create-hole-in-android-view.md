title: Create "hole" in android view
date: 2016-01-09 19:42:59
tags:
- Android
---

So now I'm doing Android development and on my first task was to make a camera view that have on overlay on top of it, but with a hole that acts as a camera vieport or viewfinder. I've seen other app have this feature so I thought there must be some tutorial or code snippet that I can learn from out there, and there is, but most of them are not obvious or doesn't quite fit in with my requirement.

And the search goes on and on, up to the point where I promise myself that if I somehow manage to pull this off I'm going to blog about it. And here it is, the simplest way I find to create a *"hole"* on an android view.
<!-- more -->

Basically what I end up doing is extending a [ViewGroup](http://developer.android.com/reference/android/view/ViewGroup.html) and override the [onDraw](http://developer.android.com/reference/android/view/View.html#onDraw%28android.graphics.Canvas%29) method to draw a clearing to create a *"hole"*. A code snippet worth a thousand words.

{% include_code lang:java android-view-hole/MyActivity.java %}

{% include_code lang:java android-view-hole/CameraView.java %}

{% include_code lang:java android-view-hole/Viewport.java %}

{% include_code lang:xml android-view-hole/my_activity.xml %}
