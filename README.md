## AllayDuplicate
A simple Minecraft Spigot plugin which adds the 1.19.1 allay duplication mechanic to 1.19.

Limitations:
* If an allay is not holding any item and is given an amethyst shard to duplicate it, a ghost item is created in its hand (even though the interact event gets cancelled).
* Since the server doesn't know the length of music discs, simply having a music disc inside a close by jukebox counts as that jukebox "playing" the disc (using the `Jukebox.isPlaying()` method).

If you know how to solve any of these issues, a PR is more than welcome :)



