Dust and Ash — machine attachments

Editable models:
  centrifuge_accelerator_1.bbmodel through centrifuge_accelerator_4.bbmodel
  automatic_milling_recycler.bbmodel

Each project embeds its 64 x 64 material atlas and uses Java block format.
The atlas packs several surface regions; a full block face samples only 16 x 16
pixels. Every face uses exactly one texture pixel per model unit in both axes.
All geometry uses integer coordinates within [0, 16], matching a 16 x 16 x 16
block. There are no half-unit details, stretched panel textures or protrusions.
Game-ready models, textures, item parents and blockstates are under
src/main/resources/assets/dustandash. Preview PNGs are rendered in Blockbench.

Accelerators use the existing machines' metal frame language: cast plates,
four tie rods and three induction rings. Bronze, gold, uranium green and
titanium blue distinguish tiers; one to four marks on the plinth also identify them.
The rotor cover and bolts sit above the lid, avoiding coincident outer faces.

The recycler has a green top, a 5 x 5 front panel, two recovery drawers,
side vents and a rear square coupling. Model north (-Z) is its front;
south (+Z) is the only machine connection. Blockstates rotate this model.

build_models.js is a reproducible Blockbench JavaScript authoring script.
Evaluate the script in Blockbench, then call
buildDAAMachine(name, tier), using tier 1-4 for accelerators or 0 for the recycler.
An open project with the matching name is rebuilt in place, with Undo support.
After the texture has loaded, call saveDAAMachine(absoluteRepositoryRoot).
Saving writes the .bbmodel, texture and Java model. The builder checks integer
geometry, block bounds and a 1:1 UV-to-face ratio before constructing each face.

Verification: both loaders' compile and dedicated-server smoke checks; model
bounds, UV ranges, texture references and translated parameters; front, side
and rear model review. The dedicated-server checks do not cover real-player
multiplayer interaction or client GUI rendering.
