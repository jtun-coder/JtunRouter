import{U as $e,n as Ce,I as me,o as ve,q as Ne,r as Le,t as De,u as se,v as Ve,w as Se,x as Ae,y as be,z as xe,B as Ue,C as Be,D as Ee,E as Fe,F as Te,G as Pe,H as Oe,J as Re,K as Me,L as je,M as He,N as ze,O as We,P as Ze,Q as qe,T as Ge,V as Je}from"./index-CSuQBRO8.js";import{_ as z,f as Ye,D as he,k as N,W as Ke,a4 as Z,C as K,F as q,a as h,o as f,g as k,s as e,x as n,p,w as m,J as Q,Y as X,c as H,q as fe,n as ge,y as A,S as C,Z as G,a8 as M,X as ee,U as Qe,G as ke,v as Xe,$ as oe,a0 as ae,a6 as et,a9 as te,a3 as tt}from"./index-CCLY6QsW.js";/* empty css                   */import{E as le}from"./el-dialog-C8XF2JDe.js";import"./el-overlay-89ZHhflZ.js";import{E as J}from"./el-button-FQWvR2f2.js";import{a as ne,E as ie}from"./el-col-BkR5g-BW.js";import{E as F}from"./index-DuTyNaIr.js";import{v as st}from"./directive-4ZKEvCYR.js";import{E as pe}from"./el-input-CUUfrk5p.js";import{E as ue}from"./el-switch-BTV1w9Lp.js";import{E as ot}from"./el-checkbox-o8_3OQ03.js";import{E as at,a as lt}from"./el-card-fgCriugF.js";import"./request-kOUjDz9H.js";import"./index-z4wGpwji.js";import"./index-82_0c4AB.js";import"./aria-BUADUvnR.js";import"./index-APFVxEOi.js";import"./index-vNULV4St.js";import"./use-form-item-BXSSCJ3O.js";import"./constants-pKhjEyna.js";import"./index-r5W6hzzQ.js";import"./typescript-Bp3YSIOJ.js";import"./index-Bfki-cik.js";import"./isEqual-CxmsCrjH.js";import"./castArray-CsLOnyYC.js";const we="/assets/submiting-B7ULOMtL.gif";function nt(l){return{all:l=l||new Map,on:function(i,u){var r=l.get(i);r?r.push(u):l.set(i,[u])},off:function(i,u){var r=l.get(i);r&&(u?r.splice(r.indexOf(u)>>>0,1):l.set(i,[]))},emit:function(i,u){var r=l.get(i);r&&r.slice().map(function(w){w(u)}),(r=l.get("*"))&&r.slice().map(function(w){w(i,u)})}}}const j=nt(),it={key:0,class:"Installed"},rt={class:"InstallList"},ct={class:"installedLength"},dt={class:"contentDataItem"},pt={class:"ContentTop"},ut={class:"LogoBlock"},_t=["src"],mt={class:"Version"},vt={class:"ItemDetail"},ht={class:"ItemNameBlock"},ft={class:"ItemName"},gt={class:"versionName"},kt={class:"versionInfo"},wt={class:"ContentBottom"},yt={class:"ButtonBlock"},It=["onClick"],$t=["onClick"],Ct=["onClick"],Nt=["onClick"],Lt={class:"InstallIng"},Dt=["src"],Vt={class:"stateTip"},St={key:1},At={key:2,class:"installOver"},bt={key:3,class:"installFailed"},xt={__name:"InStalled",props:{InstallAppList:Array,storeAppList:Array},setup(l){let i=l;const u=()=>{},r=Ye(()=>i.InstallAppList.map(s=>{const d=i.storeAppList.find(I=>I.pkg===s.packageName);return{...s,icon:d?d.icon:s.icon}}));he(()=>i.InstallAppList,s=>{console.log(s)},{immediate:!0,deep:!0});let w=N(!0),L=Ke();const D=Z();let o=N(!1);const g=[{name:"Not DownLoaded"},{name:"DownLoading ..."},{name:"Download Completed"},{name:"Installing ..."},{name:"Install Completed"},{name:"Install Failed"}];let V=K({status:1,statusTip:"Not DownLoaded",progress:0});const S=s=>{console.log(s),w.value=!1,L.setHandleTwoPathName(s.name),j.emit("isShow",w.value),j.emit("installItem",s)},B=async s=>{try{(await $e({password:D.token,packageName:s.packageName})).code==0&&setTimeout(()=>{M.go(0)},1e3)}catch(d){console.log(d)}},O=async s=>{try{const d=await Ce({packageName:s.packageName,password:D.token});d.code==0||console.error("Error: ",d.message||"未知错误")}catch(d){console.log(d)}finally{setTimeout(()=>{S(s)},2e3)}},E=async s=>{o.value=!0;try{(await me({password:D.token,url:s.updateData.link,appInfo:{packageName:s.updateData.pkg,canonicalName:s.updateData.startup,versionCode:s.updateData.v,versionName:s.updateData.ver,name:s.updateData.app,brief:s.updateData.brief}})).code==0?a(s.updateData.pkg):(F({message:"Failed to install",type:"error",duration:2e3}),o.value=!1)}catch{o.value=!1,F({message:"Failed to install",type:"error",duration:2e3})}},a=async s=>{const d=setInterval(async()=>{try{let I=await ve({password:D.token,packageName:s});I.code==0?(V.status=I.data.status,V.statusTip=g[I.data.status].name,V.progress=(I.data.progress/I.data.total).toFixed(2)*100,(I.data.status==4||I.data.status==5)&&clearInterval(d)):(clearInterval(d),o.value=!1,F({message:"Failed to get installation progress",type:"error",duration:2e3}),M.go(0))}catch{clearInterval(d),o.value=!1,F({message:"Failed to get installation progress",type:"error",duration:2e3}),M.go(0)}},800)},v=()=>{o.value=!1,M.go(0)};return q(()=>{}),(s,d)=>{const I=ne,x=ie,t=J,$=le,_=st;return h(w)?(f(),k("div",it,[e("div",rt,[e("div",ct,n(s.$t("install.title")+"("+r.value.length+")"),1),p(x,{gutter:24,class:"contentData"},{default:m(()=>[(f(!0),k(Q,null,X(r.value,(c,T)=>(f(),H(I,{key:T,xs:24,sm:12,md:6,lg:6,xl:6},{default:m(()=>[fe((f(),k("div",dt,[e("div",pt,[e("div",ut,[e("img",{class:"appIcon",src:c.icon,alt:""},null,8,_t),e("div",mt,n("V"+c.versionName),1)]),e("div",vt,[e("div",ht,[e("div",ft,n(c.name),1),e("div",{class:ge({ItemActive:!0,activeIcon:!!c.isRun})},null,2)]),e("div",gt,n(c.versionCode),1),e("div",kt,[e("span",null,n(c.brief),1)])])]),e("div",wt,[e("div",yt,[c.isRun?(f(),k("div",{key:0,class:"DetailButton",onClick:R=>S(c)},n(s.$t("install.view")),9,It)):(f(),k("div",{key:1,class:"open",onClick:R=>O(c)},n(s.$t("install.open")),9,$t)),c.update?(f(),k("div",{key:2,class:"Update",onClick:R=>E(c)},n(s.$t("install.update")),9,Ct)):A("",!0),c.isRun?A("",!0):(f(),k("div",{key:3,class:"uninstall",onClick:R=>B(c)},n(s.$t("install.Uninstall")),9,Nt))])])])),[[_,c.isLoading]])]),_:2},1024))),128))]),_:1})]),p($,{modelValue:h(o),"onUpdate:modelValue":d[0]||(d[0]=c=>G(o)?o.value=c:o=c),"close-on-click-modal":!1,"before-close":u},{default:m(()=>[e("div",Lt,[h(V).status<4?(f(),k("img",{key:0,src:h(we),alt:""},null,8,Dt)):A("",!0),e("div",Vt,n(h(V).statusTip),1),h(V).status==1?(f(),k("div",St,n(h(V).progress+"%"),1)):A("",!0),h(V).status==4?(f(),k("div",At,[p(t,{type:"primary",onClick:v},{default:m(()=>[C("Ok")]),_:1})])):A("",!0),h(V).status==5?(f(),k("div",bt,[p(t,{type:"danger",onClick:v},{default:m(()=>[C("Ok")]),_:1})])):A("",!0)])]),_:1},8,["modelValue"])])):A("",!0)}}},Ut=z(xt,[["__scopeId","data-v-d8e142e4"]]),Bt={key:0,class:"Uninstall"},Et={class:"UNInstallList"},Ft={class:"installedLength"},Tt={class:"contentDataItem"},Pt={class:"ContentTop"},Ot={class:"LogoBlock"},Rt=["src"],Mt={class:"Version"},jt={class:"ItemDetail"},Ht={class:"ItemNameBlock"},zt={class:"ItemName"},Wt={key:0,class:"Intalled"},Zt={class:"versionName"},qt={class:"versionInfo"},Gt={class:"ContentBottom"},Jt={class:"ButtonBlock"},Yt=["onClick"],Kt={class:"InstallIng"},Qt=["src"],Xt={class:"stateTip"},es={key:1},ts={key:2,class:"installOver"},ss={key:3,class:"installFailed"},os={__name:"NotInStall",props:{StoreAppList:Array,count:Number},setup(l){const{t:i}=ee();let u=l;he(()=>u.StoreAppList,a=>{console.log(a)},{immediate:!0,deep:!0});const r=Qe();let w=N(!0);const L=Z(),D=[{name:"NotDownLoaded"},{name:"DownLoading"},{name:"downCompleted"},{name:"Installing"},{name:"InstallCompleted"},{name:"InstallFailed"}];let o=N(!1),g=K({status:1,statusTip:"Not DownLoaded",progress:0});const V=()=>{},S=async a=>{o.value=!0;try{(await me({password:L.token,url:a.link,appInfo:{packageName:a.pkg,canonicalName:a.startup,versionCode:a.v,versionName:a.ver,name:a.app,brief:a.brief}})).code==0?B(a.pkg):(F({message:"Failed to install",type:"error",duration:2e3}),o.value=!1)}catch{o.value=!1,F({message:"Failed to install",type:"error",duration:2e3})}},B=async a=>{const v=setInterval(async()=>{try{let s=await ve({password:L.token,packageName:a});s.code==0?(g.status=s.data.status,g.statusTip=i("install."+D[s.data.status].name),g.progress=Math.round(s.data.progress/s.data.total*100),(s.data.status==4||s.data.status==5)&&clearInterval(v)):(clearInterval(v),o.value=!1,F({message:"Failed to get installation progress",type:"error",duration:2e3}),r.go(0))}catch{clearInterval(v),o.value=!1,F({message:"Failed to get installation progress",type:"error",duration:2e3}),r.go(0)}},800)},O=a=>{w.value=a},E=()=>{o.value=!1,r.go(0)};return q(()=>{j.on("isShow",O)}),ke(()=>{j.off("isShow",O)}),(a,v)=>{const s=ne,d=ie,I=J,x=le;return h(w)?(f(),k("div",Bt,[e("div",Et,[e("div",Ft,n(a.$t("install.Appstore")+"("+h(u).count+")"),1),p(d,{gutter:24,class:"contentData"},{default:m(()=>[(f(!0),k(Q,null,X(h(u).StoreAppList,(t,$)=>(f(),H(s,{key:$,xs:24,sm:12,md:6,lg:6,xl:6},{default:m(()=>[e("div",Tt,[e("div",Pt,[e("div",Ot,[e("img",{class:"appIcon",src:t.icon,alt:""},null,8,Rt),e("div",Mt,n("V"+t.ver),1)]),e("div",jt,[e("div",Ht,[e("div",zt,n(t.app),1),t.install?(f(),k("div",Wt,n(a.$t("install.installed")),1)):A("",!0)]),e("div",Zt,n(t.v),1),e("div",qt,[e("span",null,n(t.brief),1)])])]),e("div",Gt,[e("div",Jt,[e("div",{class:"installation",onClick:_=>S(t)},n(a.$t("install.Installation")),9,Yt)])])])]),_:2},1024))),128))]),_:1})]),p(x,{modelValue:h(o),"onUpdate:modelValue":v[0]||(v[0]=t=>G(o)?o.value=t:o=t),"close-on-click-modal":!1,"before-close":V},{default:m(()=>[e("div",Kt,[h(g).status<4?(f(),k("img",{key:0,src:h(we),alt:""},null,8,Qt)):A("",!0),e("div",Xt,n(h(g).statusTip),1),h(g).status==1?(f(),k("div",es,n(h(g).progress+"%"),1)):A("",!0),h(g).status==4?(f(),k("div",ts,[p(I,{type:"primary",onClick:E},{default:m(()=>[C("Ok")]),_:1})])):A("",!0),h(g).status==5?(f(),k("div",ss,[p(I,{type:"danger",onClick:E},{default:m(()=>[C("Ok")]),_:1})])):A("",!0)])]),_:1},8,["modelValue"])])):A("",!0)}}},as=z(os,[["__scopeId","data-v-2e749745"]]),P=l=>(oe("data-v-39baba85"),l=l(),ae(),l),ls={class:"UrlBlock"},ns={class:"LeftUrl"},is=P(()=>e("div",{class:"urlDetail"},[e("span",null,"Support Vless/Vmess/Trojan/Hysteria2/Socks5 link import.")],-1)),rs=P(()=>e("svg",{xmlns:"http://www.w3.org/2000/svg",width:"25",height:"24",viewBox:"0 0 25 24",fill:"none"},[e("path",{d:"M12.5 22C18.0229 22 22.5 17.5229 22.5 12C22.5 6.47715 18.0229 2 12.5 2C6.97715 2 2.5 6.47715 2.5 12C2.5 17.5229 6.97715 22 12.5 22Z",stroke:"#E12D2D","stroke-width":"2","stroke-linejoin":"round"}),e("path",{d:"M15.3284 9.17188L9.67151 14.8287",stroke:"#E12D2D","stroke-width":"2","stroke-linecap":"round","stroke-linejoin":"round"}),e("path",{d:"M9.67163 9.17188L15.3285 14.8287",stroke:"#E12D2D","stroke-width":"2","stroke-linecap":"round","stroke-linejoin":"round"})],-1)),cs={class:"SwitchBlock"},ds={class:"switch"},ps={class:"switchName"},us={class:"TurnON"},_s={class:"LeftSpan"},ms={class:"RightSpan"},vs={class:"TipBottom"},hs={key:0,class:"information"},fs=P(()=>e("div",{class:"switchName"},"Information",-1)),gs={class:"BodyBlock"},ks={class:"InformationItem"},ws=P(()=>e("div",{class:"ItemName"},"Remarks:",-1)),ys={class:"ItemValue"},Is={class:"InformationItem"},$s=P(()=>e("div",{class:"ItemName"},"Address:",-1)),Cs={class:"ItemValue"},Ns={class:"InformationItem"},Ls=P(()=>e("div",{class:"ItemName"},"Protocal:",-1)),Ds={class:"ItemValue"},Vs={class:"InformationItem"},Ss=P(()=>e("div",{class:"ItemName"},"Port:",-1)),As={class:"ItemValue"},bs={class:"InformationItem"},xs=P(()=>e("div",{class:"ItemName"},"IP Address:",-1)),Us={class:"ItemValue"},Bs={class:"InformationItem"},Es=P(()=>e("div",{class:"ItemName"},"TCP Ping:",-1)),Fs={class:"ItemValue"},Ts={class:"storeItem"},Ps=P(()=>e("div",{class:"storeItemTitle"},[e("div",{class:"storeItemName"},"Configuration")],-1)),Os={key:0,class:"storeItemDetail"},Rs=["onClick"],Ms={class:"selectTip"},js={class:"SelectName"},Hs={class:"severName"},zs={class:"serverPort"},Ws={key:1,class:"noStoreNode"},Zs=P(()=>e("p",null,"Please import your node connection",-1)),qs=[Zs],Gs={class:"ImportBlock"},Js=P(()=>e("div",{class:"ImportTitle"},"Import Sharing URL",-1)),Ys={class:"ImportForm"},Ks=P(()=>e("div",{class:"ImportFormUrl"},"URL",-1)),Qs={class:"dialog-footer"},Xs={__name:"V2rayPage",props:{packName:{type:String}},setup(l){const{t:i}=ee(),u=Z();let r=N(!1),w=N(""),L=N(),D=N(!1),o=N("");const g=l,V=K([]);let S=N();const B=async t=>{if(t.guid!=S.value.guid)try{const $=await Ne({password:u.token,guid:t.guid});console.log($),$.code==0&&setTimeout(()=>{v()},1e3)}catch($){console.log($)}},O=async t=>{try{(await be({password:u.token,guid:t.guid})).code==0&&setTimeout(()=>{v()},1e3)}catch($){console.log($)}},E=()=>{r.value=!0},a=async()=>{try{const t=await Le({password:u.token,config:w.value});console.log(t),t.code==0&&v()}catch(t){console.log(t)}r.value=!1},v=async()=>{const t=await De({password:u.token});console.log(t),L.value=t.data.isRunning,S.value=s(t.data.selected,t.data.list),V.splice(0,V.length,...t.data.list),console.log(V)},s=(t,$)=>{let _=$.findIndex(c=>c.guid==t);if(_!=-1)return $[_]},d=async()=>{try{const t=await se({packageName:g.packName,password:u.token});t.code==0?M.go(0):console.error("Error: ",t.message||"未知错误")}catch(t){console.log(t)}},I=async()=>{D.value=!0;try{let t;if(L.value){if(t=await Ve({password:u.token}),t.code==0)return v(),!0;throw new Error("Setting failed")}else{if(t=await Se({password:u.token}),t.code===0)return!0;throw new Error("Setting failed")}}catch(t){return console.error(t),!1}finally{D.value=!1}},x=async()=>{try{const t=await Ae({password:u.token});t.code==0&&(o.value=t.data)}catch(t){console.log(t)}};return q(()=>{v()}),(t,$)=>{const _=ue,c=ne,T=ie,R=J,ce=pe,Y=le;return f(),k("div",null,[e("div",ls,[e("div",ns,[e("div",{class:"UrlImport",onClick:E},n(h(i)("v2ray.tip1")),1),is]),e("div",{class:"RightUrl",onClick:d},[rs,C(" "+n(t.$t("install.CloseApp")),1)])]),e("div",cs,[e("div",ds,[e("div",ps,n(t.$t("install.Switch")),1),e("div",us,[e("div",_s,n(t.$t("install.onoroff")),1),e("div",ms,[p(_,{style:{"font-size":"22px"},modelValue:h(L),"onUpdate:modelValue":$[0]||($[0]=y=>G(L)?L.value=y:L=y),"active-color":"#13ce66","inactive-color":"#ff4949",loading:h(D),"before-change":()=>I()},null,8,["modelValue","loading","before-change"])])]),e("div",vs,n(h(i)("v2ray.tip2")),1)]),h(L)?(f(),k("div",hs,[e("div",{class:"TopBlock"},[fs,e("div",{class:"conNectTest",onClick:x},"Connectivity Test")]),e("div",gs,[p(T,{gutter:24},{default:m(()=>[p(c,{xs:24,sm:12,md:12,lg:12,xl:12},{default:m(()=>{var y;return[e("div",ks,[ws,e("div",ys,n(((y=h(S))==null?void 0:y.profile.remarks)||""),1)])]}),_:1}),p(c,{xs:24,sm:12,md:12,lg:12,xl:12},{default:m(()=>{var y;return[e("div",Is,[$s,e("div",Cs,n(((y=h(S))==null?void 0:y.profile.server)||""),1)])]}),_:1})]),_:1}),p(T,{gutter:24},{default:m(()=>[p(c,{xs:24,sm:12,md:12,lg:12,xl:12},{default:m(()=>{var y;return[e("div",Ns,[Ls,e("div",Ds,n(((y=h(S))==null?void 0:y.profile.configType)||""),1)])]}),_:1}),p(c,{xs:24,sm:12,md:12,lg:12,xl:12},{default:m(()=>{var y;return[e("div",Vs,[Ss,e("div",As,n(((y=h(S))==null?void 0:y.profile.serverPort)||""),1)])]}),_:1})]),_:1}),p(T,{gutter:24},{default:m(()=>[p(c,{xs:24,sm:12,md:12,lg:12,xl:12},{default:m(()=>{var y;return[e("div",bs,[xs,e("div",Us,n(((y=h(S))==null?void 0:y.profile.server)||""),1)])]}),_:1}),p(c,{xs:24,sm:12,md:12,lg:12,xl:12},{default:m(()=>[e("div",Bs,[Es,e("div",Fs,n(h(o)==-1?"TimeOut":h(o)),1)])]),_:1})]),_:1})])])):A("",!0)]),e("div",Ts,[Ps,V.length>0?(f(),k("div",Os,[(f(!0),k(Q,null,X(V,(y,b)=>(f(),k("div",{key:b,onClick:W=>B(y),class:"storeItemLink"},[e("div",Ms,[e("div",{class:ge(["selectTipInside",{active:y.guid==h(S).guid}])},null,2)]),e("div",js,[e("div",Hs,n(y.profile.server),1),e("div",zs,n("severPort:"+y.profile.serverPort),1)]),p(R,{class:"DeleteButton",disabled:y.guid===h(S).guid,onClick:Xe(W=>O(y),["stop"])},{default:m(()=>[C("Delete")]),_:2},1032,["disabled","onClick"])],8,Rs))),128))])):(f(),k("div",Ws,qs))]),p(Y,{class:"ImportDialog",modelValue:h(r),"onUpdate:modelValue":$[4]||($[4]=y=>G(r)?r.value=y:r=y),"destroy-on-close":"",center:""},{footer:m(()=>[e("div",Qs,[e("div",{class:"cancelButton",onClick:$[2]||($[2]=y=>G(r)?r.value=!1:r=!1)},"Cancel"),e("div",{class:"ImportButton",onClick:$[3]||($[3]=y=>a())},"Import")])]),default:m(()=>[e("div",Gs,[Js,e("div",Ys,[Ks,p(ce,{class:"ImportFormInput",type:"text",placeholder:"请输入内容",modelValue:h(w),"onUpdate:modelValue":$[1]||($[1]=y=>G(w)?w.value=y:w=y)},null,8,["modelValue"])])])]),_:1},8,["modelValue"])])}}},eo=z(Xs,[["__scopeId","data-v-39baba85"]]),ye=l=>(oe("data-v-7e6a77e2"),l=l(),ae(),l),to={class:"UrlBlock"},so=ye(()=>e("div",{class:"LeftUrl"},null,-1)),oo=ye(()=>e("svg",{xmlns:"http://www.w3.org/2000/svg",width:"25",height:"24",viewBox:"0 0 25 24",fill:"none"},[e("path",{d:"M12.5 22C18.0229 22 22.5 17.5229 22.5 12C22.5 6.47715 18.0229 2 12.5 2C6.97715 2 2.5 6.47715 2.5 12C2.5 17.5229 6.97715 22 12.5 22Z",stroke:"#E12D2D","stroke-width":"2","stroke-linejoin":"round"}),e("path",{d:"M15.3284 9.17188L9.67151 14.8287",stroke:"#E12D2D","stroke-width":"2","stroke-linecap":"round","stroke-linejoin":"round"}),e("path",{d:"M9.67163 9.17188L15.3285 14.8287",stroke:"#E12D2D","stroke-width":"2","stroke-linecap":"round","stroke-linejoin":"round"})],-1)),ao={class:"SwitchBlock"},lo={class:"switch"},no={class:"switchName"},io={class:"TurnON"},ro={class:"LeftSpan"},co={class:"RightSpan"},po={class:"TipBottom"},uo={key:0,style:{width:"100%",height:"50px","margin-top":"30px"}},_o={style:{width:"100%",height:"100%","margin-top":"20px"}},mo={__name:"AListLite",props:{packName:{type:String}},setup(l){const{t:i}=ee(),u=Z(),r=N(null),w=N(!1),L=l,D=N("");let o=N(!1);const g=N(),V=async()=>{try{const d=await xe({password:u.token});d.code==0&&(g.value=d.data)}catch(d){console.log(d)}},S=window.location.port;let B="";S=="2000"?B=`${window.location.hostname+":5244"}`:S=="8080"&&(B=`${window.location.hostname.replace(/^(.*?)(\..*)$/,"$1-alist$2")+":8080"}`),console.log(B);const O="http://"+B,E=async()=>{try{const d=await Ue({password:u.token,adminPass:D.value});console.log(d),d.code==0&&(w.value=!1,F.success(i("AListLite.passsuc")))}catch(d){console.log(d)}},a=()=>{r.value&&r.value.contentWindow&&r.value.contentWindow.document.querySelectorAll(".hope-c-PJLV-iekLzst-css").forEach(x=>{x.style.display="none"})},v=async()=>{console.log(L.packName);try{const d=await se({packageName:L.packName,password:u.token});d.code==0?M.go(0):console.error("Error: ",d.message||"未知错误")}catch(d){console.log(d)}},s=async()=>{o.value=!0;try{let d;if(g.value){if(d=await Be({password:u.token}),d.code==0)return V(),!0;throw new Error("Setting failed")}else{if(d=await Ee({password:u.token}),d.code===0)return V(),!0;throw new Error("Setting failed")}}catch(d){return console.error(d),!1}finally{o.value=!1}};return q(()=>{V(),r.value&&r.value.contentWindow&&a()}),(d,I)=>{const x=ue,t=J,$=pe,_=le;return f(),k("div",null,[e("div",to,[so,e("div",{class:"RightUrl",onClick:v},[oo,C(" "+n(d.$t("install.CloseApp")),1)])]),e("div",ao,[e("div",lo,[e("div",no,n(d.$t("install.Switch")),1),e("div",io,[e("div",ro,n(d.$t("install.onoroff")),1),e("div",co,[p(x,{style:{"font-size":"22px"},modelValue:g.value,"onUpdate:modelValue":I[0]||(I[0]=c=>g.value=c),"active-color":"#13ce66","inactive-color":"#ff4949",loading:h(o),"before-change":()=>s()},null,8,["modelValue","loading","before-change"])])]),e("div",po,n(h(i)("AListLite.tip1")),1)])]),g.value?(f(),k("div",uo,[p(t,{type:"primary",style:{height:"56px","font-size":"24px"},onClick:I[1]||(I[1]=c=>w.value=!0)},{default:m(()=>[C(n(h(i)("AListLite.pass")),1)]),_:1})])):A("",!0),e("div",_o,[e("iframe",{ref_key:"myIframe",ref:r,style:{width:"100%",height:"500px","border-radius":"12px"},src:O,frameborder:"0"},null,512)]),p(_,{modelValue:w.value,"onUpdate:modelValue":I[3]||(I[3]=c=>w.value=c),style:{"border-radius":"10px",height:"200px"},width:"500"},{default:m(()=>[e("h2",null,n(h(i)("AListLite.pass")),1),p($,{type:"password",style:{height:"48px"},modelValue:D.value,"onUpdate:modelValue":I[2]||(I[2]=c=>D.value=c),placeholder:"New Password"},null,8,["modelValue"]),p(t,{type:"primary",style:{"margin-top":"10px"},onClick:E},{default:m(()=>[C("确定")]),_:1})]),_:1},8,["modelValue"])])}}},vo=z(mo,[["__scopeId","data-v-7e6a77e2"]]),Ie=l=>(oe("data-v-0af39c25"),l=l(),ae(),l),ho={class:"UrlBlock"},fo=Ie(()=>e("div",{class:"LeftUrl"},[e("div",{class:"urlDetail"},[e("span")])],-1)),go=Ie(()=>e("svg",{xmlns:"http://www.w3.org/2000/svg",width:"25",height:"24",viewBox:"0 0 25 24",fill:"none"},[e("path",{d:"M12.5 22C18.0229 22 22.5 17.5229 22.5 12C22.5 6.47715 18.0229 2 12.5 2C6.97715 2 2.5 6.47715 2.5 12C2.5 17.5229 6.97715 22 12.5 22Z",stroke:"#E12D2D","stroke-width":"2","stroke-linejoin":"round"}),e("path",{d:"M15.3284 9.17188L9.67151 14.8287",stroke:"#E12D2D","stroke-width":"2","stroke-linecap":"round","stroke-linejoin":"round"}),e("path",{d:"M9.67163 9.17188L15.3285 14.8287",stroke:"#E12D2D","stroke-width":"2","stroke-linecap":"round","stroke-linejoin":"round"})],-1)),ko={class:"SwitchBlock"},wo={class:"switch"},yo={class:"switchName"},Io={class:"TurnON"},$o={class:"SwitchBlock"},Co={class:"switch"},No={class:"switchName"},Lo={class:"upload"},Do={class:"Importconfig"},Vo={class:"UpFile"},So={__name:"OpenVpn",props:{packName:{type:String}},setup(l){ee();const i=Z(),u=N(""),r=N(""),w=l,L=()=>{document.getElementById("fileInput").click()},D=K([]),o=async()=>{try{const a=await Fe({password:i.token});a.code==0&&D.splice(0,D.length,...a.data)}catch(a){console.log(a)}},g=a=>{const v=a.target.files[0];if(console.log(v),v){r.value=v.name;const s=new FileReader;s.onloadend=()=>{u.value=s.result.split(",")[1],console.log(u.value)},s.readAsDataURL(v)}},V=async()=>{if(u.value)try{(await Te({password:i.token,config:u.value,name:r.value})).code==0&&(r.value="",u.value="",o())}catch(a){console.log(a)}},S=async a=>{try{(await Pe({password:i.token,uuid:a})).code==0&&setTimeout(()=>{o()},1600)}catch(v){console.log(v)}},B=async a=>{try{(await Oe({password:i.token,uuid:a})).code==0&&setTimeout(()=>{o()},1600)}catch(v){console.log(v)}},O=async()=>{try{const a=await se({packageName:w.packName,password:i.token});a.code==0?M.go(0):console.error("Error: ",a.message||"未知错误")}catch(a){console.log(a)}},E=async a=>{try{(await Re({password:i.token,uuid:a})).code==0&&setTimeout(()=>{o()},1600)}catch(v){console.log(v)}};return q(()=>{o()}),(a,v)=>{const s=ne,d=ie,I=ot,x=J;return f(),k("div",null,[e("div",ho,[fo,e("div",{class:"RightUrl",onClick:O},[go,C(" "+n(a.$t("install.CloseApp")),1)])]),e("div",ko,[e("div",wo,[e("div",yo,n(a.$t("install.openvpninstance")),1),e("div",Io,[p(d,{gutter:0,style:{width:"100%"},class:"TurnHead"},{default:m(()=>[p(s,{xs:4,sm:4,md:4,lg:4,xl:4},{default:m(()=>[C(n(a.$t("install.name")),1)]),_:1}),p(s,{xs:3,sm:3,md:3,lg:3,xl:3},{default:m(()=>[C(n(a.$t("install.enabled")),1)]),_:1}),p(s,{xs:3,sm:3,md:3,lg:3,xl:3},{default:m(()=>[C(n(a.$t("install.started")),1)]),_:1}),p(s,{xs:4,sm:4,md:4,lg:4,xl:4},{default:m(()=>[C(n(a.$t("install.startorstop")),1)]),_:1}),p(s,{xs:3,sm:3,md:3,lg:3,xl:3},{default:m(()=>[C(n(a.$t("install.port")),1)]),_:1}),p(s,{xs:3,sm:3,md:3,lg:3,xl:3},{default:m(()=>[C(n(a.$t("install.ProtocolName")),1)]),_:1}),p(s,{xs:4,sm:4,md:4,lg:4,xl:4},{default:m(()=>[C("    "+n(a.$t("install.actions")),1)]),_:1})]),_:1}),(f(!0),k(Q,null,X(D,(t,$)=>(f(),H(d,{gutter:0,key:$,class:"TurnItem"},{default:m(()=>[p(s,{xs:4,sm:4,md:4,lg:4,xl:4,class:"spanName"},{default:m(()=>[C(n(t.name),1)]),_:2},1024),p(s,{xs:3,sm:3,md:3,lg:3,xl:3},{default:m(()=>[p(I,{modelValue:t.servers[0].enabled,"onUpdate:modelValue":_=>t.servers[0].enabled=_,size:"large"},null,8,["modelValue","onUpdate:modelValue"])]),_:2},1024),p(s,{xs:3,sm:3,md:3,lg:3,xl:3},{default:m(()=>[C(n(t.active==!0?"Yes":"No"),1)]),_:2},1024),p(s,{xs:4,sm:4,md:4,lg:4,xl:4},{default:m(()=>[t.active==!1?(f(),H(x,{key:0,class:"StartButton",onClick:v[0]||(v[0]=_=>S(a.itqem.uuid))},{default:m(()=>[C("Start")]),_:1})):(f(),H(x,{key:1,class:"StopButton",onClick:_=>B(t.uuid)},{default:m(()=>[C("Stop")]),_:2},1032,["onClick"]))]),_:2},1024),p(s,{xs:3,sm:3,md:3,lg:3,xl:3},{default:m(()=>[C(n(t.servers[0].port),1)]),_:2},1024),p(s,{xs:3,sm:3,md:3,lg:3,xl:3},{default:m(()=>[C(n(t.servers[0].useUdp==!0?"UDP":"-"),1)]),_:2},1024),p(s,{xs:4,sm:4,md:4,lg:4,xl:4},{default:m(()=>[p(x,{disabled:t.active,class:"DeleteButton",onClick:_=>E(t.uuid)},{default:m(()=>[C("Delete")]),_:2},1032,["disabled","onClick"])]),_:2},1024)]),_:2},1024))),128))])])]),e("div",$o,[e("div",Co,[e("div",No,n(a.$t("install.uploadfile")),1),e("div",Lo,[e("input",{style:{display:"none"},type:"file",id:"fileInput",onChange:g,accept:".ovpn"},null,32),e("div",Do,[fe(e("input",{type:"text",disabled:"",class:"configName","onUpdate:modelValue":v[1]||(v[1]=t=>r.value=t)},null,512),[[et,r.value]]),p(x,{class:"ImportConfigButton",onClick:L},{default:m(()=>[C(n(a.$t("install.SelectFileupload")),1)]),_:1})]),e("div",Vo,[p(x,{class:"UploadButton",onClick:v[2]||(v[2]=t=>V())},{default:m(()=>[C(n(a.$t("install.uploadfiles")),1)]),_:1})])])])])])}}},Ao=z(So,[["__scopeId","data-v-0af39c25"]]),U=[];for(let l=0;l<256;++l)U.push((l+256).toString(16).slice(1));function bo(l,i=0){return(U[l[i+0]]+U[l[i+1]]+U[l[i+2]]+U[l[i+3]]+"-"+U[l[i+4]]+U[l[i+5]]+"-"+U[l[i+6]]+U[l[i+7]]+"-"+U[l[i+8]]+U[l[i+9]]+"-"+U[l[i+10]]+U[l[i+11]]+U[l[i+12]]+U[l[i+13]]+U[l[i+14]]+U[l[i+15]]).toLowerCase()}let de;const xo=new Uint8Array(16);function Uo(){if(!de){if(typeof crypto>"u"||!crypto.getRandomValues)throw new Error("crypto.getRandomValues() not supported. See https://github.com/uuidjs/uuid#getrandomvalues-not-supported");de=crypto.getRandomValues.bind(crypto)}return de(xo)}const Bo=typeof crypto<"u"&&crypto.randomUUID&&crypto.randomUUID.bind(crypto),_e={randomUUID:Bo};function Eo(l,i,u){if(_e.randomUUID&&!i&&!l)return _e.randomUUID();l=l||{};const r=l.random||(l.rng||Uo)();return r[6]=r[6]&15|64,r[8]=r[8]&63|128,bo(r)}const re=l=>(oe("data-v-babd8bc2"),l=l(),ae(),l),Fo={class:"UrlBlock"},To=re(()=>e("div",{class:"LeftUrl"},[e("div",{class:"urlDetail"},[e("span")])],-1)),Po=re(()=>e("svg",{fill:"none",height:"24",viewBox:"0 0 25 24",width:"25",xmlns:"http://www.w3.org/2000/svg"},[e("path",{d:"M12.5 22C18.0229 22 22.5 17.5229 22.5 12C22.5 6.47715 18.0229 2 12.5 2C6.97715 2 2.5 6.47715 2.5 12C2.5 17.5229 6.97715 22 12.5 22Z",stroke:"#E12D2D","stroke-linejoin":"round","stroke-width":"2"}),e("path",{d:"M15.3284 9.17188L9.67151 14.8287",stroke:"#E12D2D","stroke-linecap":"round","stroke-linejoin":"round","stroke-width":"2"}),e("path",{d:"M9.67163 9.17188L15.3285 14.8287",stroke:"#E12D2D","stroke-linecap":"round","stroke-linejoin":"round","stroke-width":"2"})],-1)),Oo={class:"SwitchBlock"},Ro={class:"switch"},Mo={class:"switchName"},jo={class:"TurnON"},Ho={class:"title"},zo={style:{display:"flex","flex-wrap":"wrap","justify-content":"space-between"}},Wo=["onClick"],Zo=["onClick"],qo=["onClick"],Go=["onClick"],Jo={key:0,class:"maskBox"},Yo={class:"maskContent"},Ko={class:"maskTitle"},Qo=re(()=>e("path",{d:"M4 4L20 20",stroke:"#333333","stroke-linecap":"round","stroke-linejoin":"round","stroke-width":"2"},null,-1)),Xo=re(()=>e("path",{d:"M4 20L20 4",stroke:"#333333","stroke-linecap":"round","stroke-linejoin":"round","stroke-width":"2"},null,-1)),ea=[Qo,Xo],ta={class:"UpFile"},sa={class:"btnBox"},oa={__name:"Frpc",props:{packName:{type:String}},setup(l){const{t:i}=ee(),u=Z(),r=l,w=N(""),L=N(i("install.frpAdd")),D=N(!1),o=N(""),g=N(!1),V=()=>{t(),L.value=i("install.frpAdd"),o.value="",D.value=!0},S=async _=>{L.value=i("install.frpEdit");const c=await Me({password:u.token,uid:_});if(c.code==0){const{data:T}=c;w.value=T.uid,g.value=T.connecting,o.value=T.name,v.value=T.cfg,D.value=!0}else F.error(c.message)},B=()=>{L.value=" ",o.value="",D.value=!1},O=()=>{B()},E=K([]),a=async()=>{try{const _=await je({password:u.token});_.code==0?E.splice(0,E.length,..._.data):F.error(_.message)}catch(_){console.log(_)}},v=N(""),s=async()=>{try{w.value==""&&(w.value=Eo());let _;L.value==i("install.frpEdit")?_=await He({config:{uid:w.value,name:o.value,cfg:v.value,connecting:g.value},password:u.token}):_=await ze({config:{uid:w.value,name:o.value,cfg:v.value,connecting:g.value},password:u.token}),_.code==0?(o.value="",v.value="",g.value=!1,B(),a()):F.error(_.message)}catch(_){console.log(_)}},d=async _=>{try{const c=await We({password:u.token,uid:_});c.code==0?setTimeout(()=>{a()},1600):F.error(c.message)}catch(c){console.log(c)}},I=async _=>{try{const c=await Ze({password:u.token,uid:_});c.code==0?setTimeout(()=>{a()},1600):F.error(c.message)}catch(c){console.log(c)}},x=async()=>{try{const _=await se({packageName:r.packName,password:u.token});_.code==0?M.go(0):console.error("Error: ",_.message||"未知错误")}catch(_){console.log(_)}},t=()=>{v.value=`[common]
server_addr = server address
server_port = 7000

[adb]
type = tcp
local_ip = 127.0.0.1
local_port = 5555
remote_port = 5555

[screen]
type = tcp
local_ip = 127.0.0.1
local_port = 7007
remote_port = 5556`};t();const $=async _=>{try{(await qe({password:u.token,uid:_})).code==0&&setTimeout(()=>{a()},1600)}catch(c){console.log(c)}};return q(()=>{a()}),(_,c)=>{const T=J,R=ue,ce=at,Y=lt,y=pe;return f(),k("div",null,[e("div",Fo,[To,e("div",{class:"RightUrl",onClick:x},[Po,C(" "+n(_.$t("install.CloseApp")),1)])]),e("div",Oo,[e("div",Ro,[e("div",Mo,[C(n(_.$t("install.frpTitle"))+" ",1),p(T,{class:"UploadButton",type:"primary",onClick:V},{default:m(()=>[C(n(_.$t("install.frpAdd")),1)]),_:1})]),e("div",jo,[(f(!0),k(Q,null,X(E,b=>(f(),H(ce,{class:"box-card",key:b.uid},{default:m(()=>[e("div",Ho,[e("span",null,n(b.name),1),p(R,{modelValue:b.connecting,"onUpdate:modelValue":W=>b.connecting=W,style:{"margin-right":"13px"},disabled:""},null,8,["modelValue","onUpdate:modelValue"])]),e("div",zo,[e("div",{class:"DeleteButton",onClick:W=>$(b.uid)},n(_.$t("install.frpDelete")),9,Wo),e("div",{class:"DeleteButton edit",onClick:W=>S(b.uid)},n(_.$t("install.frpEdit")),9,Zo),b.connecting==!0?(f(),k("div",{key:0,class:"DeleteButton",onClick:W=>I(b.uid)},n(_.$t("install.frpStop")),9,qo)):A("",!0),b.connecting==!1?(f(),k("div",{key:1,class:"DeleteButton edit",onClick:W=>d(b.uid)},n(_.$t("install.frpStart")),9,Go)):A("",!0)])]),_:2},1024))),128))])])]),D.value?(f(),k("div",Jo,[e("div",Yo,[e("div",Ko,[e("span",null,n(L.value),1),(f(),k("svg",{fill:"none",height:"16",viewBox:"0 0 24 24",width:"16",xmlns:"http://www.w3.org/2000/svg",onClick:O},ea))]),e("div",ta,[p(Y,{label:_.$t("install.frpStatus")},{default:m(()=>[p(R,{modelValue:g.value,"onUpdate:modelValue":c[0]||(c[0]=b=>g.value=b),"active-color":"#13ce66","inactive-color":"#ff4949"},null,8,["modelValue"])]),_:1},8,["label"]),p(Y,{label:_.$t("install.frpName")},{default:m(()=>[p(y,{modelValue:o.value,"onUpdate:modelValue":c[1]||(c[1]=b=>o.value=b),placeholder:_.$t("install.frpContent"),style:{width:"100%"}},null,8,["modelValue","placeholder"])]),_:1},8,["label"]),v.value?(f(),H(Y,{key:0,label:_.$t("install.frpConfig")},{default:m(()=>[p(y,{modelValue:v.value,"onUpdate:modelValue":c[2]||(c[2]=b=>v.value=b),rows:20,placeholder:_.$t("install.frpContent"),style:{width:"100%"},type:"textarea"},null,8,["modelValue","placeholder"])]),_:1},8,["label"])):A("",!0)]),e("div",sa,[p(T,{class:"UploadButton",type:"primary",onClick:s},{default:m(()=>[C(n(L.value),1)]),_:1})])])])):A("",!0)])}}},aa=z(oa,[["__scopeId","data-v-babd8bc2"]]),la={key:0,class:"storeItem"},na={key:1},ia={__name:"InstallDetaIl",setup(l){const i=N(null);let u=N(!0);const r=o=>{i.value=o,D.value=L[o.name]},w=o=>{u.value=o},L={v2rayNG:te(eo),AListLite:te(vo),OpenVPN:te(Ao),Frpc:te(aa)};let D=N();return q(()=>{j.on("isShow",w),j.on("installItem",r)}),ke(()=>{j.off("isShow",w),j.off("installItem",r)}),(o,g)=>h(u)?A("",!0):(f(),k("div",la,[e("div",null,[i.value?(f(),H(tt(h(D)),{key:0,packName:i.value.packageName},null,8,["packName"])):(f(),k("p",na,"请等待安装项加载..."))])]))}},ra=z(ia,[["__scopeId","data-v-dfb9bf66"]]),ca={class:"storePage"},da={__name:"StorePage",setup(l){const i=Z();let u=N([]),r=N([]),w=N(0);const L=async()=>{try{const o=await Ge({password:i.token});if(o.code==0)u.value=o.data;else{console.error("Failed to fetch installed app list with code:",o.code);return}const g=await Je();if(g.data.code==0)r.value=g.data.rows,w.value=g.data.count;else{console.error("Failed to fetch store app list with code:",g.data.code);return}D()}catch(o){console.error("Error fetching app lists:",o)}},D=()=>{u.value.forEach(o=>{const g=r.value.find(V=>V.pkg===o.packageName);g&&g.v>o.versionCode?(o.update=!0,o.updateData=g):o.update=!1}),r.value.forEach(o=>{o.install=u.value.some(g=>g.packageName===o.pkg)})};return L(),(o,g)=>(f(),k("div",ca,[p(Ut,{storeAppList:h(r),InstallAppList:h(u)},null,8,["storeAppList","InstallAppList"]),p(as,{StoreAppList:h(r),count:h(w)},null,8,["StoreAppList","count"]),p(ra)]))}},Ta=z(da,[["__scopeId","data-v-341ad370"]]);export{Ta as default};