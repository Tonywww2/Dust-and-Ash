// Run in Blockbench's JavaScript environment. Each machine is a separate editable Java block project.
// One block = 16 model units. Every face uses exactly one texture pixel per model unit.
// The 64px atlas packs multiple material regions; it does not increase per-face pixel density.
globalThis.buildDAAMachine = function(name, tier) {
    const existing = ModelProject.all.find(project => project.name === name);
    if (existing) existing.select();
    else if (!Project || Project.name !== name || Cube.all.length) newProject(Formats.java_block);
    Undo.initEdit({elements: Cube.all.slice(), textures: Texture.all.slice(), outliner: true, uv_mode: true});
    Cube.all.slice().forEach(cube => cube.remove());
    Group.all.slice().forEach(group => group.remove());
    Texture.all.slice().forEach(texture => texture.remove(true));
    Project.name = name;
    Project.texture_width = Project.texture_height = 64;
    Project.credit = 'Dust and Ash — machine attachments';

    const canvas = document.createElement('canvas');
    canvas.width = canvas.height = 64;
    const ctx = canvas.getContext('2d');
    const accents = ['#bf8548', '#d4ac58', '#87ad61', '#8fc8d0'];
    const accent = accents[Math.max(0, tier - 1)];
    const materials = [
        ['#555c60', '#778086', '#31383d'], // frame
        ['#aab0ae', '#d6d9d3', '#747e80'], // cast housing
        ['#292e31', '#444d50', '#171b20'], // recess
        [accent, '#e5d6a8', '#634a36'], // coil and tier colour
        ['#65766b', '#a8ba95', '#3b4944'], // milling green
        ['#68675f', '#9e9b88', '#3a3c38'], // bin
        ['#a2c578', '#d1e7a6', '#475b3d'], // control indicator
        ['#c29452', '#e6bb6a', '#725832'] // caution
    ];
    materials.forEach((colours, i) => {
        const x = (i % 4) * 16, y = Math.floor(i / 4) * 16;
        ctx.fillStyle = colours[0]; ctx.fillRect(x,y,16,16);
        ctx.fillStyle = colours[1]; ctx.fillRect(x,y,16,1); ctx.fillRect(x,y,1,16);
        ctx.fillStyle = colours[2]; ctx.fillRect(x+15,y+1,1,15); ctx.fillRect(x+1,y+15,15,1);
        // Small, deliberate clusters on the same one-unit pixel grid as the geometry.
        ctx.fillStyle = colours[1]; ctx.fillRect(x+3,y+4,3,1);
        ctx.fillStyle = colours[2]; ctx.fillRect(x+9,y+11,4,1);
    });
    ctx.fillStyle = '#30383c'; ctx.fillRect(0,32,64,32);
    // All dedicated regions have exactly the dimensions of their target face.
    // 11 x 11 selector: five 1px buttons at 2px pitch, with a 1px outer margin.
    ctx.fillStyle = '#111b1c'; ctx.fillRect(0,32,11,11);
    for (let y=0;y<5;y++) for(let x=0;x<5;x++) {
        ctx.fillStyle = ((x+y)%4===0) ? '#c1985a' : '#a8c489';
        ctx.fillRect(1+x*2,33+y*2,1,1);
    }
    // Two narrow drawers on a 12 x 3 face.
    ctx.fillStyle = '#aab0a8'; ctx.fillRect(16,32,12,3);
    ctx.fillStyle = '#3b4548'; ctx.fillRect(16,33,12,1);
    ctx.fillStyle = '#4a5353'; ctx.fillRect(20,32,4,1); ctx.fillRect(20,34,4,1);
    // 14 x 14 side vent region.
    ctx.fillStyle = '#6a7376'; ctx.fillRect(32,32,14,14);
    for(let y=0;y<6;y++) {
        ctx.fillStyle = '#20272b'; ctx.fillRect(34,34+y*2,10,1);
    }
    // 8 x 8 rotor cover; 10 x 8 rear coupling. No high-resolution image is squeezed onto them.
    ctx.fillStyle = '#323b40'; ctx.fillRect(48,32,8,8);
    ctx.fillStyle = accent; ctx.fillRect(49,33,6,6);
    ctx.fillStyle = '#172127'; ctx.fillRect(50,34,4,4);
    ctx.fillStyle = '#a7b6b0'; ctx.fillRect(51,35,2,2);
    ctx.fillStyle = '#555c60'; ctx.fillRect(48,48,10,8);
    ctx.fillStyle = '#bf8548'; ctx.fillRect(49,49,8,6);
    ctx.fillStyle = '#172127'; ctx.fillRect(50,50,6,4);
    // Tier marks are painted directly into the 16 x 3 plinth face, without offset overlay cubes.
    ctx.fillStyle = '#555c60'; ctx.fillRect(0,48,16,3);
    ctx.fillStyle = '#778086'; ctx.fillRect(0,48,16,1);
    for(let i=0;i<Math.max(1,tier);i++) {
        ctx.fillStyle = accent; ctx.fillRect(4+i*2,49,1,2);
    }
    ctx.fillStyle = '#555c60'; ctx.fillRect(16,48,16,1);
    ctx.fillStyle = '#c29452'; ctx.fillRect(18,48,12,1);

    const texture = new Texture({name: name + '.png', id:'0', namespace:'dustandash', folder:'block/machine_attachments'}).fromDataURL(canvas.toDataURL()).add(false);
    texture.particle = true;
    const groups = {};
    function box(groupName, label, from, to, material, special) {
        if (!from.concat(to).every(v => Number.isInteger(v) && v >= 0 && v <= 16)
                || !from.every((v,i) => v < to[i])) throw new Error('Off-grid geometry: ' + label);
        if (!groups[groupName]) groups[groupName] = new Group({name:groupName,origin:[8,8,8]}).init();
        const cube = new Cube({name:label,from,to,autouv:0}).addTo(groups[groupName]).init();
        const size = to.map((v,i)=>v-from[i]);
        Object.entries(cube.faces).forEach(([face, data]) => {
            const dims = (face==='up'||face==='down') ? [size[0],size[2]]
                : (face==='east'||face==='west') ? [size[2],size[1]] : [size[0],size[1]];
            const u=(material%4)*16, v=Math.floor(material/4)*16;
            data.texture = texture.uuid;
            data.uv = [u,v,u+dims[0],v+dims[1]];
            if (special && special[face]) data.uv = special[face];
            if (Math.abs(data.uv[2]-data.uv[0]) !== dims[0]
                    || Math.abs(data.uv[3]-data.uv[1]) !== dims[1]) {
                throw new Error('Non-unit pixel density: ' + label + '/' + face);
            }
        });
        return cube;
    }
    if (tier) {
        // A heavy plinth, four tie rods and stacked induction rings around a recessed rotor.
        box('housing','lower cast plinth',[0,0,0],[16,3,16],0,
            {north:[0,48,16,51],south:[0,48,16,51]});
        box('housing','upper cast plate',[0,13,0],[16,15,16],1);
        box('rotor','central spindle',[5,3,5],[11,13,11],2);
        for (let x of [1,13]) for(let z of [1,13]) {
            box('housing','vertical tie rod',[x,3,z],[x+2,13,z+2],0);
            box('housing','top bolt',[x,15,z],[x+2,16,z+2],2);
        }
        for(let y of [4,7,10]) {
            box('induction_rings','front coil',[3,y,2],[13,y+2,4],3);
            box('induction_rings','rear coil',[3,y,12],[13,y+2,14],3);
            box('induction_rings','left coil',[2,y,4],[4,y+2,12],3);
            box('induction_rings','right coil',[12,y,4],[14,y+2,12],3);
        }
        box('housing','top rotor cover',[4,15,4],[12,16,12],2,{up:[48,32,56,40]});
    } else {
        // Compact filing cabinet: raised panel at front, two drawers, visible rear-only coupling.
        box('cabinet','base',[0,0,0],[16,1,16],0,{north:[16,48,32,49]});
        box('cabinet','main housing',[1,1,1],[15,15,15],1,
            {east:[32,32,46,46],west:[32,32,46,46]});
        box('cabinet','top cap',[0,15,0],[16,16,16],4);
        for (let x of [0,14]) {
            box('cabinet','front edge rail',[x,1,0],[x+2,15,2],0);
            box('cabinet','rear edge rail',[x,1,14],[x+2,15,16],0);
        }
        box('control','5 by 5 selector panel',[2,4,0],[13,15,1],2,{north:[0,32,11,43]});
        box('storage','double recovery drawer',[2,1,0],[14,4,1],5,{north:[16,32,28,35]});
        box('connector','rear coupling flange',[3,5,15],[13,13,16],0,{south:[48,48,58,56]});
        box('control','status lamp',[13,12,0],[14,14,1],6);
    }
    const displays = {
        thirdperson_righthand:{rotation:[75,45,0],translation:[0,2.5,0],scale:[0.375,0.375,0.375]},
        firstperson_righthand:{rotation:[0,45,0],scale:[0.4,0.4,0.4]},
        ground:{translation:[0,3,0],scale:[0.25,0.25,0.25]},
        gui:{rotation:[30,225,0],scale:[0.625,0.625,0.625]},
        fixed:{scale:[0.5,0.5,0.5]}
    };
    Object.entries(displays).forEach(([slot,data])=>Project.display_settings[slot] = new DisplaySlot(slot,data));
    Canvas.updateAll();
    Undo.finishEdit('Rebuild machine on the 16-unit pixel grid', {elements:Cube.all, textures:Texture.all, outliner:true, uv_mode:true});
    return {name, cubes:Cube.all.length, texture:texture.uuid};
};

globalThis.saveDAAMachine = function(root) {
    const fs = require('fs'), path=require('path');
    const name=Project.name;
    const modelPath=path.join(root,'Models','machine_attachments',name+'.bbmodel');
    const texturePath=path.join(root,'src/main/resources/assets/dustandash/textures/block/machine_attachments',name+'.png');
    fs.mkdirSync(path.dirname(texturePath),{recursive:true});
    fs.writeFileSync(texturePath,Buffer.from(Texture.all[0].getDataURL().split(',')[1],'base64'));
    Project.save_path=modelPath;
    const model=JSON.parse(Codecs.java_block.compile({cube_name:true}));
    model.textures={'0':'dustandash:block/machine_attachments/'+name,particle:'dustandash:block/machine_attachments/'+name};
    fs.writeFileSync(path.join(root,'src/main/resources/assets/dustandash/models/block',name+'.json'),JSON.stringify(model,null,2)+'\n');
    fs.writeFileSync(modelPath,Codecs.project.compile());
    Project.saved=true;
    return {name, cubes:model.elements.length, modelPath};
};
