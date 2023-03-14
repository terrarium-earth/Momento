<center>

## Momento

[![Requires ResourcefulLib](https://cdn.discordapp.com/attachments/1073717602880327761/1073717942014972034/RLib_vector.svg)](https://modrinth.com/mod/resourceful-lib)
[![Made by Terrarium](https://cdn.discordapp.com/attachments/1073717602880327761/1073718144910233691/Terrarium_vector.svg)](https://discord.terrarium.earth)
[![curseforge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/curseforge_vector.svg)](https://www.curseforge.com/minecraft/mc-mods/momento)
[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/momento)
<hr>

### 📖About 📖

<hr>
</center>

Momento is a pack maker tool that enables you to create audio logs in game!
Right clicking on the player item or triggering the `/dialogue` command will playback audio
alongside subtitiles that will match the audio.

<center>

### 🔧 How to Use (Wiki) 🔧

<hr>
</center>

Every dialogue has at least 2 components:

- A `.srt` file that will server as the subtitle for your dialogue
- A `.json` file that will dictate how your dialogue will look and sound

Everything for this mod can be contained with a **Resourcepack** as the **config files** are completely clientside.

> Note: While the files for the dialogues are client side, the mod itself is still required on both client and server!

#### SubRip Subtitle Files (`.srt`)

`.srt` files are easy to make, you can use a tool like Descript to record your audio for your dialogue and create the
subtitles at the same time.
(Video coming soon on how to explain)

If you want to support us and make your life easier at the same time, you can use our link to start
a [free trial with Descript](https://codexadrian.tech/descript).
We're going to be using this tool in our projects, and we highly recommend it!

In your resourcepack, this `.srt` file needs to be placed in `/assets/{id}/momento/srt/{lang}`

> Note: Momento supports multi-language subtitles! replace `{lang}` with the identifier for whatever language you'd like
> your subtitiles
> to support, like `en_us` for English. (These identifiers are the same as what you would name the `.json` file for
> langs in resourece packs)

#### Dialogue Files

`[dialogue_name].json` files should be placed in `/assets/{modid}/momento/dialogue`

By default, the minimum needed to configure it is a name, a reference to a sound file, the volume, and the reference to
the `.srt` file we just talked about.
That will look something like this:

```json
{
  "name": "Demonstration - by CodexAdrian",
  "sound": "momento:demo",
  "volume": 1,
  "srt": "momento:demo"
}
```

> Note, the sound reference I used here is kinda short for a typical sound. Yours might look something
> like `example:example.dialogues.dialogue_intro`.
> The reference will be whatever you called it in your sounds.json file prefixed with the id of your resourcepack.

Much more beyond this is configurable however. Lets go down the list.

<details>
<summary>
Name
</summary>

The name here actually follows the standard format for Text Components in minecraft, so they are configurable in all the
ways that
the title command typically is. You can use [this wiki](https://minecraft.fandom.com/wiki/Raw_JSON_text_format) to learn
how to further
configure the name of your dialogue
</details>

<details>
<summary>
Icon
</summary>

This dictates what the texture of the item will look like. Its a string to chose between one of our 5 built in options.
The options are:
- `"BRASS_TAPE_RECORDER"`
- `"HAND_HELD_SPEAKER"`
- `"TEAL_PHONOGRAPH"`
- `"WOODEN_RADIO"`
- `"PURPLE_PHONOGRAPH"`

The default option if left blank is `"BRASS_TAPE_RECORDER"`
</details>

<details>
<summary>
Display
</summary>

There are 2 types of displays you can use for your subtitiles that affect how the subtitile text is rendered on screen.

#### Canvas

Canvas display is the default, it renders a box of a single color behind the text. By default it looks like this:

```json
{
  "display": {
    "color": "black",
    "alpha": 128,
    "width": 50,
    "padding": 10,
    "margin": {
      "top": 0,
      "bottom": -80,
      "left": 0,
      "right": 0
    }
  }
}
```

Most of these values are self-explanatory. Padding and margin both use the same type of config, so a valid padding and
margin config can either look like this

```json
{
  "margin": {
    "top": 10,
    "bottom": 10,
    "left": 0,
    "right": 0
  }
}
```

or this

```json
{
  "margin": 0
}
```

The single number margin/padding will apply that number of margin or padding to all 4 sides.

Turns out the color system in Resourceful Lib is really complicated, and has ALOT of configurable options. Its very nice and flexible for those of you
who want to go crazy with pretty colors. I recommend using one of the preconfigured options
[here](https://github.com/Team-Resourceful/ResourcefulLib/blob/1.19.3/common/src/main/java/com/teamresourceful/resourcefullib/common/color/ConstantColors.java).
If you want more explanation on the colors, join our [Discord](https://discord.terrarium.earth) and bother ThatGravyBoat, apparently there are a dozen different
ways you can configure a color. there's also a rainbow option maybe? idk join and find out ;).

#### Texture

Alternatively, if you're goin for a different vibe, you can replace the canvas with a rendered texture. Its a little complicated, so stick with me here.

The texture for the dialogue needs 3 components. Think of it like a sandwich. The top bun `"top"`, which part gets rendered on top, the meat `"background"`, the part that gets rendered
multiple times in a stack to fit the height of the box, and the bottom bum `"bottom"` which renders on the bottom. Together, these components allow a very flexible text
box experience. That looks something like this.

```json
{
  "display": {
    "top": {
      "texture": "{modid}:guis/momento.png",
      "x": 0,
      "y": 0,
      "u": 0,
      "v": 0,
      "width": 256,
      "height": 256,
      "textureWidth": 256,
      "textureHeight": 256
    },
    "background": {
      // same as top, but slightly different
    },
    "bottom": {
      //same as top, but slightly different
    }
  },
  ...
}
```

Lets run through these values

`"x"` and `"y"`
- Imagine you have a selection box that you're dragging to select a group of pixels in a paint. These coordinates are where it will start selecting from.

`"u"` and `"v"`
- Ima be honest, this is rendering stuff, you probably shouldn't change this unless you know what you're doing. These values have a default value of 0 and 0, 
you can leave these out and it'll fill them in with the default values automatically.

`"width"` and `"height"`
- Remember how I said we're drawing a selection box starting at x and y? This is the size of that selection box.

`"textureWidth"` and `"textureHeight"`
- This is the dimensions of the entire image that the `"texture"` refers to. You should leave these values blank, as all gui files in Minecraft are typically 256x256 in size.

The recommended way of doing this for you texture artists is to draw your textbox in a single file, make the top, background and bottom all refer to the same file and change the
'selection box' to encapsulate different regions of the texture for the top, bottom and background sections. Your selection boxes can be touching each other,
so it's fine to make the texture look like 1 connected box and then layer the selection boxes stacked next to each other.

</details>

<center>
<hr>

[![Use code "Terrarium" for 25% off at BisectHosting](https://www.bisecthosting.com/images/CF/RiseNFall_Kingdom/BH_RNF_PromoCard.png)](http://bisecthosting.com/terrarium)

<hr>

## ✨Socials✨

[![youtube-plural](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/social/youtube-plural_vector.svg)](https://youtube.terrarium.earth)
[![twitch-plural](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/social/twitch-plural_vector.svg)](https://www.twitch.tv/terrariumearth)
[![twitter-plural](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/social/twitter-plural_vector.svg)](https://twitter.terrarium.earth)
[![kofi-plural](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/donate/kofi-plural_vector.svg)](https://kofi.terrarium.earth)
[![discord-plural](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/social/discord-plural_vector.svg)](https://discord.terrarium.earth)
[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/available/modrinth_vector.svg)](https://modrinth.com/user/Terrarium)
[![curseforge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/available/curseforge_vector.svg)](https://www.curseforge.com/members/terrariumearth/projects)
<hr>
</center>


