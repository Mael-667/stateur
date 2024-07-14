//<input type="file" id="filepicker" name="filelist" webkitdirectory="" multiple="">

let nb = 77
let fichier = ""
let trueurllist = {};
let ttul = [];
let urllist = {};
let video = [];
let channel = [];

async function nerf(form){

    /* reponse = path du dossier;{path: path du fichier,
                                    name: nom du fichier,
                                    directory: true/false pr aider au tri
                                    icon: array(height) de array(width) de array qui représentent les couleurs des pixels
                                    height: hauteur icone
                                    width: largeur
                                    };*
    */
   amorcage()
}


function amorcage(){
    let body = document.querySelector("body");
    let bg = document.createElement("div");
    body.appendChild(bg)
    let div = document.createElement("div");
    div.addEventListener("click", v => {
        v.stopPropagation()
    })
    bg.appendChild(div)
    bg.id = "bg"
    bg.setAttribute("onclick", "despawn()")
    bg.style = `position: absolute;
                z-index: 667;
                height: 100%;
                width: 100%;
                display: flex;
                justify-content: center;
                align-items: center;
                background-color: #0000006e;`
    div.style = `display: flex;
                flex-direction: column;
                justify-content: flex-start;
                overflow-y: hidden;
                height: 70vh;
                width: 90vw;
                background: rgb(38, 38, 60);
                padding: 1em;
                border-radius: 0.3em;
                border: 1px solid black;`
    div.id = "div"
    var requete = new XMLHttpRequest();
        requete.open("POST",  "http://localhost:8000/stat");
        let post = 'explorer,default'
        requete.responseType = "blob";
        requete.send(post)
        requete.onreadystatechange = function() {
            if (this.readyState == XMLHttpRequest.DONE && this.status == 200) {
                let aya = ""
                const reader = new FileReader()
                reader.readAsText(this.response, "UTF-16")
                reader.addEventListener("loadend", v => {
                    aya = reader.result
                    explorer(aya, div)
                })
                
            }
        }
}

function despawn(){
    document.getElementById("bg").outerHTML = ""
}

function reqexpl(target){
    let id = target.id
    let div = document.getElementById('div')
    div.classList.add('loading')
    var requete = new XMLHttpRequest();
    requete.open("POST",  "http://localhost:8000/stat");
    //requete.setRequestHeader("Content-Type", "application/json");
    let post = "explorer," + id
    requete.responseType = "blob";
    requete.send(post)
    requete.onreadystatechange = async function() {
        if (this.readyState == XMLHttpRequest.DONE && this.status == 200) {
            div.classList.remove("loading")
            let aya = ""
            const reader = new FileReader()
            reader.readAsText(this.response, "UTF-16")
            reader.addEventListener("loadend", v => {
                aya = reader.result
                explorer(aya, div)
            })
            
        }
    }
}

function fileselect(name, path){
    fc[0] = name;
    fc[1] = path;
    let box = document.getElementById("box")
    box.innerHTML = ""
    let selected = document.createElement("span")
    box.appendChild(selected)
    selected.classList.add("barrepath")
    selected.innerText = fc[0];
    selected.id = fc[1];
    let input = document.createElement("span")
    box.appendChild(input)
    input.innerText = "Selectionner"
    input.classList.add("btn")
    input.setAttribute("onclick", "selectionne(document.querySelector('.barrepath'))")
}

let fc = ["", ""];

function explorer(rep, div){
    div.innerHTML = ""
    let bruh = eval(rep)
    let chemin = document.createElement("div")
    chemin.classList.add("filepath")
    let path = bruh.shift().split("\\");
    let id = "";
    for(let i = 0; i < path.length; i++){
        id+= path[i] + "\\"
        let sl = document.createElement("span"); 
        sl.innerHTML = path[i] + "\\"; 
        sl.id = id;
        sl.classList.add("bblien")
        sl.setAttribute("onclick", "reqexpl(this)")
        chemin.appendChild(sl);
    }
    div.appendChild(chemin)

    let fichiers = document.createElement("div")
    fichiers.style = `  display: flex;
                        flex-wrap: wrap;
                        height: -webkit-fill-available;
                        overflow-y: scroll;
                        margin-bottom: 1em;
                        min-width: 100%;
                        align-items: flex-start;
                        align-content: flex-start;`
    div.appendChild(fichiers)
    let files = [];
    for(let k = 0; k < bruh.length; k++){
        let aya = bruh[k]
        var lf = document.createElement("span");
        lf.classList.add("filecentre")
        var canvas = document.createElement("canvas");
        lf.appendChild(canvas)
        let span = document.createElement("span");
        span.style= "padding-left: 0.3em;"
        span.innerText = aya.name
        lf.appendChild(span)
        lf.setAttribute("onclick", `fileselect("${aya.name}", "${aya.path.replaceAll("\\", "\\\\")}")`)
        if(eval(aya.directory)){
            fichiers.appendChild(lf);
            lf.id = aya.path
            lf.setAttribute("ondblclick", "reqexpl(this)")
        } else {
            files[files.length] = lf
        }
        //fichiers.appendChild(lf)
        var ctx = canvas.getContext("2d");
        ctx.canvas.width = aya.width
        ctx.canvas.height = aya.height
        let couleur = eval(aya.icon);
        for(let i = 0; i < aya.height; i++){
            for(let j = 0; j < aya.width; j++){
                    ctx.fillStyle = `rgb(${couleur[i][j][0]} ${couleur[i][j][1]} ${couleur[i][j][2]}/${Math.floor(couleur[i][j][3]*100/255)}%)`;
                    ctx.fillRect(1*i, 1*j, 1, 1)
            }
        }
    }
    files.forEach(v => fichiers.appendChild(v))

    let box = document.createElement("div")
    box.style = `display: flex;
                flex-direction: row;
                align-items: center;`
    div.appendChild(box)
    box.id = "box";
    fileselect(fc[0], fc[1])

}

function selectionne(div){
    console.log(div)
    if(div.innerText.endsWith(".html")){
        despawn()
        document.getElementById("resultat").style = "display: flex;"
        document.querySelector(".centre").style = "display: none; !important"
        stat(div.id)
    } else if(div.innerText.endsWith("messages")){
        csvtostat(div.id)
    }
}

let oui = ""
function stat(file){
    var requete = new XMLHttpRequest();
        requete.open("POST",  "http://localhost:8000/stat");
        let post = `yt,stat,${file}`
        requete.responseType = "blob"
        requete.send(post)
        requete.onreadystatechange = function() {
            if (this.readyState == XMLHttpRequest.DONE && this.status == 200) {
                /*for(let a = 0; a < charset.length; a++){
                    const reader = new FileReader()
                    //console.log(this.response.indexOf("×"))
                    //console.log(this.response.video[0])
                    reader.readAsText(this.response, charset[a])
                    reader.addEventListener("loadend", v => {
                        //let rep = JSON.parse(reader.result)
                        console.log(reader.result.charAt(0) +" "+charset[a])
                    })
                }*/
                const reader = new FileReader()
                reader.readAsText(this.response, "UTF-16")
                reader.addEventListener("loadend", v => {
                    let rep = JSON.parse(reader.result)
                    video = rep.video
                    channel = rep.chaine
                    trueurllist = rep.ttul
                    affichage(video, channel)
                    funfact(video)
                })
            }
        }
    //console.log(`temps d'execution d'affichage ${Date.now() - la2}ms`)
}

function ouvre(elmnt){
    let nom = elmnt.nextElementSibling.innerText
    elmnt.parentElement.classList.toggle("charge");
    elmnt.style = "transform: rotate(90deg);"
    elmnt.attributes[1].textContent = "return ferme(this)"
    let tl = document.createElement("div") //truelist pov
    tl.classList.add("tl")
    for(let i = 0; i < nb*3; i++){
        if(trueurllist[i][1].search(`${nom}`) > -1){
            tl.innerHTML+=`<div class="tlrslt"> ${trueurllist[i][1].match("<a.*?</a>")[0]} : ${trueurllist[i][0]} vues</div>`
        }
    }
    tl.innerHTML+=`<div class="yar"><button class="yarbtn" id="${nom}" onclick="ttcal(this)">Tout charger ? 😎</span></button>`
    elmnt.parentElement.appendChild(tl)
}

function ferme(elmnt){
    elmnt.parentElement.classList.toggle("charge");
    elmnt.style = "transform: rotate(0deg);"
    elmnt.parentElement.querySelector(".tl").remove()
    elmnt.attributes[1].textContent = "return ouvre(this)"
}

function affichage(vid, channel){
    let video = document.getElementById("video");
    let chan = document.getElementById("channel");
    let pov = video.innerHTML;
    let pdv = chan.innerHTML;
    for(let i = 0; i < nb; i++){
        let un = vid.shift();
        let deux = channel.shift()
        pov+=`<div class="ans"> ${un[1]} : ${un[0]} vues</div>`;
        pdv+=`<div class="ans"> <span class="md" onclick="return ouvre(this)">▶</span> ${deux[1]} : ${deux[0]} vues</div>`;
    }
    video.innerHTML= pov;
    chan.innerHTML= pdv;

}



function recalcul(form){
    nb = form.value;
    affichage(video, channel)
}


function spoil(div){
    let bruh = div.parentElement.children[1];
    bruh.classList.toggle("spoiler")
}

function emoji2moi(e){
    let emoji = ["😎","🤡","😳","🏴‍☠️","💀","✌","✌🏾","💪","👶","🦈","👋","🖖🏻","🎈","ouiettoi"]
    let target = e.childNodes
    target[1].innerText = emoji[Math.floor(Math.random() * emoji.length)]
}

function triv2(list){
    let laultime = Date.now()

    function fusion(g, d){
        let ans = [];
        let mdr = g.length + d.length
            for(let i = 0; i < mdr; i++){
                if(g[0] == undefined){
                    ans[i] = d.shift()
                } else if(d[0] == undefined){
                    ans[i] = g.shift()
                } else if( g[0][0] < d[0][0]) {
                    ans[i] = d.shift()
                } else {
                    ans[i] = g.shift()
                }
            }
        return ans
    }

    function defusion(liste){
        if(liste.length == 1){
            return liste;
        } else {
            let g = liste.splice(0, liste.length/2) ;
            let d = liste;
            return fusion(defusion(g), defusion(d))
        }
    }

    let ans = defusion(list)

    console.log(`temps d'execution de tri ultime ${Date.now() - laultime}ms et il a trié ${ans.length} objets 😎💪✌🏾`)
    return ans;
}


function funfact(liste){
    let total = 0;
    liste.forEach(v => total+=v[0])
    let sueur = document.getElementById("sueur")
    sueur.innerHTML += `<span class="spoil"><button class="spoilbtn" onclick="return spoil(this)">SPOIL</button><span class="spoiler spoil">T'as tema ${total} vids en tout. Si on part du principe qu'une vid = 10mn en moyenne, t'as gaché ${total*10}mn de ta vie en tout, soit ${total*10/60}h, soit ${Math.floor(total*10/60/24)}j 🤡</span></span>`
    sueur.innerHTML += `<div class="bbassaed assaed" onclick="return switchent()">go next life</div>`
}

function ttcal(ca){
    let res = trueurllist;
    let nom = ca.id
    let daron = ca.parentElement;
    tl = daron.parentElement;
    tl.innerHTML = ""
    res.forEach((v) => {
        if(v[1].search(`${nom}`) > -1){
            tl.innerHTML+=`<div class="tlrslt"> ${v[1].match("<a.*?</a>")[0]} : ${v[0]} vues</div>`
        }
    })
}

function json2array(json){
    let array = [];
    for(let key in json){
        array[array.length] = [json[key], key]
    }
    return array;
}

function trifusion(nb){
    let aya = nb;
    let isso = [];
    /*for(let p = 0; p < aya; p++){
        isso[p] = Math.floor(Math.random() * (aya*2))
    }
    let avantdcp = Date.now()
    let issu = tribasique(isso)
    console.log(`temps d'execution de l'autre ${Date.now() - avantdcp}ms`)
    console.log(verif(issu))*/

    let isse = [];
    for(let p = 0; p < aya; p++){
        isse[p] = Math.floor(Math.random() * (aya*2))
    }
    let mtn = Date.now()
    let issou = defusion(isse)
    console.log(`temps d'execution de tri fusion de moi ${Date.now() - mtn}ms 😳`)
    console.log(issou.length)
    console.log(verif(issou))

    function verif(liste){
        for(let i = 0; i < liste.length; i++){
            if(liste[i] > liste[i+1]){
                console.log(liste)
                return false
            }
            return true
        }
    }

    function tribasique(liste){
        let ans = []
        let maxvalue = -1;
        let co = 0;
        for(let i = 0; i < liste.length; i++){
            maxvalue = -1 //pov je peux juste mettre liste[0] ici mais g peur de tout casser flm de retester
            for(let key = 0; key < liste.length; key++){
                if(liste[key] > maxvalue){
                    maxvalue = liste[key];
                    co = key;
                }
            }
            ans.unshift(liste.splice(co, 1)[0])
        }
        return ans;
    }

    function fusion(g, d){
        let ans = [];
        let mdr = g.length + d.length
        let ig = 0;
        let id = 0;
        for(let i = 0; i < mdr; i++){
            if(g[ig] > d[id] || ig == g.length){
                ans[i] = d[id]
                id++
            } else {
                ans[i] = g[ig]
                ig++
            }
        }
        return ans
    }

    function defusion(liste){
        if(liste.length == 1){
            return liste;
        } else {
            let g = liste.splice(0, liste.length/2) ;
            let d = liste;
            return fusion(defusion(g), defusion(d));
        }
    }
}


/*let messages = 0;
let mots = 0;
let liste2mot = {};
let emotes = 0;
let liste2emote = {};
let lettres =0;
let liste2lettre = {};*/

let sdk = document.getElementById("sdk")

function tema(){
    document.querySelector("body").classList.add("bresom")
    let bbmain = document.querySelector(".bbmain2")
    bbmain.classList.toggle("bouge")
    bbmain.style = "display: flex;"
    document.getElementById("bougepitie").style = "display: none;"

    sdk.innerText = "Commencement du tri de moi"

    let stats = document.createElement("div")
    stats.classList.add("stats")
    liste2mot = triv2(json2array(liste2mot))
    liste2emote = triv2(json2array(liste2emote))
    liste2lettre = triv2(json2array(liste2lettre)) 

    sdk.innerText = "Pov c'est dejà fini😎"

    stat2div(liste2mot, "mot", stats)
    stat2div(liste2lettre, "lettre", stats)
    stat2div(liste2emote, "emote", stats)
    bbmain.appendChild(stats)

    let statsinutiles = document.createElement("div")
    statsinutiles.classList.add("stats")
    statsinutiles.classList.add("bouge")
    let h = horairepref(heatmap)[1]
    let si = [`T'as posté ${messages} messages en tout`,
              `T'envoies ${mots/messages} mots par message en moyenne`,
              `Tu mets ${emotes/messages} emotes par message`,
              `Ton horaire préférée c'est le ${num2jour(h[0])} à ${h[1]}h`
             ]
    phrase2div(si, "Stats inutiles", "StatsI",statsinutiles)
    bbmain.appendChild(statsinutiles)

    sdk.innerText = "La heatmap se dessine ✌"

    let ccanvas = document.createElement("div")
    ccanvas.classList.add("statscanvas")
    //ccanvas.classList.add("bouge")
    drawheatmap(ccanvas, 24, heatmap)
    let width = ccanvas.querySelector("canvas").width
    let ouiettoi = document.createElement("div")
    ouiettoi.classList.add("heure")
    ouiettoi.style = `width: ${width}px;`
    ouiettoi.innerHTML+= "<span>Minuit</span><span>6h</span><span>Midi</span><span>18h</span><span>Minuit</span>"
    ccanvas.appendChild(ouiettoi)
    let titre = document.createElement("h2")
    titre.innerText = "Heatmap de quand tu envoies tes msg"
    titre.classList.add("titrecanvas")
    ccanvas.appendChild(titre)
    let aide = document.createElement("div")
    aide.innerHTML = `<div class="pointtier1"><div class="pointtier2">!</div></div><span>
            <form method="POST" id="form" onsubmit="return redraw(this);">Afficher la heatmap entre le <input class="date" id="date1" type="date"> et le <input class="date" id="date2" type="date"> ? (il faudra trier ${messages} fichiers donc ça va prendre un peu de temps) <input type="submit" value="Afficher 😎" id="nptbtn"></form>
            </span>`
    aide.classList.add("pointtier0")
    ccanvas.appendChild(aide)
    statsinutiles.appendChild(ccanvas)
}


function drawheatmap(destination, pixel, heatmap){
    var canvas = document.createElement("canvas");
    var div = document.createElement("div");
    div.classList.add("canvasconteneur")
    div.innerHTML = '<div class="listej"><span>Lundi -></span><br><span>Mardi -></span><br><span>Mercredi -></span><br><span>Jeudi -></span><br><span>Vendredi -></span><br><span>Samedi -></span><br><span>Dimanche -></span><br></div>'
    div.appendChild(canvas)
    destination.appendChild(div)
    var ctx = canvas.getContext("2d");
    let pxmin = pixel/6
    ctx.canvas.width = pixel*24
    ctx.canvas.height = pixel*7
    let max = horairepref(heatmap)[0];
    for(let i = 0; i < heatmap.length; i++){
        for(let j = 0; j < heatmap[i].length; j++){
            for(let k = 0; k < heatmap[i][j].length; k++){
                let couleur = heatmap[i][j][k] * 255 / max;
                ctx.fillStyle = `rgb(${couleur}, ${couleur}, ${couleur})`;
                ctx.fillRect(pixel*j + k*pxmin, pixel*i, pxmin, pixel)
            }
        }
    }
}

function horairepref(heatmap){
    let max = 0;
    let horaire = []
    for(let i = 0; i < heatmap.length; i++){
        for(let j = 0; j < heatmap[i].length; j++){
            for(let k = 0; k < heatmap[i][j].length; k++){
                if(heatmap[i][j][k] > max){
                    max = heatmap[i][j][k];
                    horaire = [i,j,k]
                }
            }
        }
    }
    console.log([max, horaire])
    return [max, horaire];
}

function phrase2div(liste, nom, clas, destination){
    let div = document.createElement("div")
    div.classList.add(clas, "resultat2")
    div.innerHTML+=`<h2>${nom}</h2>`
    for(let i = 0; i < liste.length; i++){
        div.innerHTML+= `<div class="ans"> ${liste[i]} </div>`
    }
    destination.appendChild(div)
}

function stat2div(liste, nom, destination){
    let div = document.createElement("div")
    div.classList.add(nom, "resultat2")
    div.innerHTML+=`<h2>Top ${nom}s :</h2>`
    for(let i = 0; i < 10; i++){
        div.innerHTML+=`<div class="ans">${liste[i][1]} : ${liste[i][0]}</div>`
    }
    let pov = 0;
    liste.forEach(v => pov+=v[0])
    div.innerHTML+=`<div class="ans">Total : ${pov} ${nom}s dont ${liste.length} différent(e)s</div>`
    destination.appendChild(div)
}


let doss;
async function pickdoss(ca){
    amorcage()
}

let id = [];
let timestamp = [];
let message = [];
let attachment = [];
let messagent = []
let messages = 0;
let mots = 0;
let liste2mot = {};
let emotes = 0;
let liste2emote = {};
let lettres =0;
let liste2lettre = {};


function csvtostat(file){
    var requete = new XMLHttpRequest();
        requete.open("POST",  "http://localhost:8000/stat");
        let post = `disc,stat,${file}`
        requete.responseType = "blob"
        requete.send(post)
        requete.onreadystatechange = function() {
            if (this.readyState == XMLHttpRequest.DONE && this.status == 200) {
                const reader = new FileReader()
                reader.readAsText(this.response, "UTF-16")
                reader.addEventListener("loadend", v => {
                    let rep = JSON.parse(reader.result)
                    console.log(rep)
                })
            }
        }
}

let assaed = document.querySelector(".assaed");
assaed.addEventListener("click", (e) => {
    switchent();
    //body.classList.add("bresom")
})

function switchent(){
    document.querySelector(".main").classList.toggle("bouge")
    let body = document.querySelector(".main2")
    body.classList.toggle("bouge");
    assaed.classList.toggle("lekheyestperdu")
    //assaed.classList.toggle("bouge");
}

function gauche(ent){
    let bbs = ent.parentElement.children;
    bbs[0].style = "display: flex !important;"
    for(let i = 0; i < bbs.length; i++){
        if(bbs[i].className.search("bouge") + bbs[i].className.search("conteneurfleche") == -2){
            bbs[i].classList.toggle("bouge")
            bbs[i-1].classList.toggle("bouge")
            if(i = 2){
                bbs[1].style = "display: none;"
            }
            break;
        }
    }
}

function droite(ent){
    let bbs = ent.parentElement.children;
    bbs[1].style = "display: flex !important;"
    for(let i = 0; i < bbs.length; i++){
        if(bbs[i].className.search("bouge") + bbs[i].className.search("conteneurfleche") == -2){
            bbs[i].classList.toggle("bouge")
            bbs[i+1].classList.toggle("bouge")
            if(i = bbs.length){
                bbs[0].style = "display: none;"
            }
            break;
        }
    }
}

function num2jour(num){
    switch(num){
        case 0:
            return "Lundi"
            break;
        case 1:
            return "Mardi"
            break;
        case 2: 
            return "Mercredi"
            break;
        case 3:
            return "Jeudi"
            break;
        case 4:
            return "Vendredi"
            break;
        case 5:
            return "Samedi"
            break;
        case 6:
            return "Dimanche"
            break;
    }
}

let trie = false;
function redraw(ca){
    try{
    console.log(ca)
    if(ca.date1.value != "" && ca.date2.value != ""){
        if(trie == false){
            messagent = triv2(messagent)
            trie = true
        }
        let canvas = document.querySelector("canvas")
        let depart = new Date(ca.date1.value).valueOf()
        let arrivee = new Date(ca.date2.value).valueOf()
        let heatmap = nouvellemap()
        console.log("sueur")
        let i = recherche(messagent, depart, 0, messagent.length)
        pov = new Date(messagent[i][0]).valueOf()
        while(new Date(messagent[i][0]).valueOf() < arrivee && i != messagent.length-1){
            let pov = new Date(messagent[i][0]).valueOf();
            mapchaleur(heatmap, pov)
            i++;
        }
        console.log(heatmap)
        var ctx = canvas.getContext("2d");
        pixel = 24
        let pxmin = pixel/6
        ctx.canvas.width = pixel*24
        ctx.canvas.height = pixel*7
        let max = horairepref(heatmap)[0];
        for(let i = 0; i < heatmap.length; i++){
            for(let j = 0; j < heatmap[i].length; j++){
                for(let k = 0; k < heatmap[i][j].length; k++){
                    let couleur = heatmap[i][j][k] * 255 / max;
                    ctx.fillStyle = `rgb(${couleur}, ${couleur}, ${couleur})`;
                    ctx.fillRect(pixel*j + k*pxmin, pixel*i, pxmin, pixel)
                }
            }
        }
    }
    } catch(err) {
        console.log(err)
    }
    return false;
}


function nouvellemap(){
    let heatmap = []
    for(let jour = 0; jour < 7; jour++){
        heatmap[jour] = [];
        for(let heure = 0; heure < 24; heure++){
            heatmap[jour][heure] = [];
            for(let minute = 0; minute < 6; minute++){
                heatmap[jour][heure][minute] = 0
            }
        }
    }
    return heatmap;
}

function recherche(liste, objectif, g, d){
    let pov = Math.floor((g+d)/2);
    let date = new Date(liste[pov][0]).valueOf()
    let datemas1 = new Date(liste[pov+1][0]).valueOf()
    if(objectif < date && objectif > datemas1){
        return pov
    } else if(date != objectif){
        console.log(date, objectif)
        if(date > objectif){
            return recherche(liste, objectif, pov, d)
        } else {
            return recherche(liste, objectif, g,pov)
        }
    } else {
        return pov
    }
}

function proccherche(liste, objectif, g, d){
    let avantdcp = Date.now()
    for(let i = 0; i < 20; i++){
        let pov = Math.floor((g+d)/2);
        console.log(pov)
        let date = new Date(liste[pov][0]).valueOf()
        let datemas1 = new Date(liste[pov+1][0]).valueOf()
        if(objectif < date && objectif > datemas1){
            console.log(liste[pov])
        } else if(date != objectif){
            console.log(date, objectif, datemas1)
            if(liste[pov][0] > objectif){
                g=pov
            } else {
                d=pov
            }
        } else {
            console.log(liste[pov])
        }
    }
    console.log(`temps d'execution de l'autre ${Date.now() - avantdcp}ms`)
}




let charset = ["Big5",
"Big5-HKSCS",
"CESU-8",
"EUC-JP",
"EUC-KR",
"GB18030",
"GB2312",
"GBK",
"IBM-Thai",
"IBM00858",
"IBM01140",
"IBM01141",
"IBM01142",
"IBM01143",
"IBM01144",
"IBM01145",
"IBM01146",
"IBM01147",
"IBM01148",
"IBM01149",
"IBM037",
"IBM1026",
"IBM1047",
"IBM273",
"IBM277",
"IBM278",
"IBM280",
"IBM284",
"IBM285",
"IBM290",
"IBM297",
"IBM420",
"IBM424",
"IBM437",
"IBM500",
"IBM775",
"IBM850",
"IBM852",
"IBM855",
"IBM857",
"IBM860",
"IBM861",
"IBM862",
"IBM863",
"IBM864",
"IBM865",
"IBM866",
"IBM868",
"IBM869",
"IBM870",
"IBM871",
"IBM918",
"ISO-2022-CN",
"ISO-2022-JP",
"ISO-2022-JP-2",
"ISO-2022-KR",
"ISO-8859-1",
"ISO-8859-13",
"ISO-8859-15",
"ISO-8859-16",
"ISO-8859-2",
"ISO-8859-3",
"ISO-8859-4",
"ISO-8859-5",
"ISO-8859-6",
"ISO-8859-7",
"ISO-8859-8",
"ISO-8859-9",
"JIS_X0201",
"JIS_X0212-1990",
"KOI8-R",
"KOI8-U",
"Shift_JIS",
"TIS-620",
"US-ASCII",
"UTF-16",
"UTF-16BE",
"UTF-16LE",
"UTF-32",
"UTF-32BE",
"UTF-32LE",
"UTF-8",
"windows-1250",
"windows-1251",
"windows-1252",
"windows-1253",
"windows-1254",
"windows-1255",
"windows-1256",
"windows-1257",
"windows-1258",
"windows-31j",
"x-Big5-HKSCS-2001",
"x-Big5-Solaris",
"x-euc-jp-linux",
"x-EUC-TW",
"x-eucJP-Open",
"x-IBM1006",
"x-IBM1025",
"x-IBM1046",
"x-IBM1097",
"x-IBM1098",
"x-IBM1112",
"x-IBM1122",
"x-IBM1123",
"x-IBM1124",
"x-IBM1129",
"x-IBM1166",
"x-IBM1364",
"x-IBM1381",
"x-IBM1383",
"x-IBM29626C",
"x-IBM300",
"x-IBM33722",
"x-IBM737",
"x-IBM833",
"x-IBM834",
"x-IBM856",
"x-IBM874",
"x-IBM875",
"x-IBM921",
"x-IBM922",
"x-IBM930",
"x-IBM933",
"x-IBM935",
"x-IBM937",
"x-IBM939",
"x-IBM942",
"x-IBM942C",
"x-IBM943",
"x-IBM943C",
"x-IBM948",
"x-IBM949",
"x-IBM949C",
"x-IBM950",
"x-IBM964",
"x-IBM970",
"x-ISCII91",
"x-ISO-2022-CN-CNS",
"x-ISO-2022-CN-GB",
"x-iso-8859-11",
"x-JIS0208",
"x-JISAutoDetect",
"x-Johab",
"x-MacArabic",
"x-MacCentralEurope",
"x-MacCroatian",
"x-MacCyrillic",
"x-MacDingbat",
"x-MacGreek",
"x-MacHebrew",
"x-MacIceland",
"x-MacRoman",
"x-MacRomania",
"x-MacSymbol",
"x-MacThai",
"x-MacTurkish",
"x-MacUkraine",
"x-MS932_0213",
"x-MS950-HKSCS",
"x-MS950-HKSCS-XP",
"x-mswin-936",
"x-PCK",
"x-SJIS_0213",
"x-UTF-16LE-BOM",
"X-UTF-32BE-BOM",
"X-UTF-32LE-BOM",
"x-windows-50220",
"x-windows-50221",
"x-windows-874",
"x-windows-949",
"x-windows-950",
"x-windows-iso2022jp"]