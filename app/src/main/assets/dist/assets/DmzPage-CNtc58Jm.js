import{_ as w,a4 as z,k as i,a1 as I,o as M,g as V,s as e,x as n,p as d,a as v,Z as f,w as c,S as Z}from"./index-CCLY6QsW.js";import{E as x,a as B}from"./el-col-BkR5g-BW.js";import{E as C}from"./el-input-CUUfrk5p.js";import{E as L}from"./el-switch-BTV1w9Lp.js";import{m as g}from"./index-CSuQBRO8.js";import{E as b}from"./index-DmH9s9cX.js";import{E as m}from"./index-DuTyNaIr.js";import"./index-82_0c4AB.js";import"./typescript-Bp3YSIOJ.js";import"./index-Bfki-cik.js";import"./index-APFVxEOi.js";import"./constants-pKhjEyna.js";import"./use-form-item-BXSSCJ3O.js";import"./request-kOUjDz9H.js";import"./directive-4ZKEvCYR.js";import"./aria-BUADUvnR.js";const k={class:"OnlineClient"},O={class:"OnlineClientTop"},U={class:"OnlineHeader"},T={class:"headerLeft"},N={class:"OnlineBody"},P={class:"SmallText"},R={class:"UpgradeDuring"},$={class:"duringLeft"},A={class:"leftTop"},H={class:"duringRight"},j={class:"IpAddress"},q={class:"OnlineBody"},F={class:"UpgradeDuring"},G={class:""},J={__name:"DmzPage",setup(K){const h=z();let r=i(""),t=i(""),a=i(""),_=i("");I(()=>{r.value=h.globalData.logon_pwd,y(),console.log(a.value,t.value,"222")});const y=async()=>{const o={cmd:"154",logon_pwd:r.value},s=await g(o,"readdmz");console.log(s,"2222"),s.code==200&&(_.value=s.dmz.device_name,a.value=s.dmz.device_ip,t.value=s.dmz.enabled==1),console.log(a.value,t.value)},D=async()=>{const o=b.service({target:"body",text:"Save DMZ settings..."});try{const s={cmd:"155",logon_pwd:r.value,device_name:_.value,device_ip:a.value,enabled:t.value==!0?1:0};(await g(s,"setdmz")).code==200?m.success("Save successfully"):m.error("Save failed")}catch{m.error("Save failed")}finally{o.close()}};return(o,s)=>{const u=L,E=C,p=B,S=x;return M(),V("div",null,[e("div",k,[e("div",O,[e("div",U,[e("div",T,n(o.$t("system.dmz")),1)]),e("div",N,[e("div",P,n(o.$t("system.descE")),1),e("div",R,[e("div",$,[e("div",A,n(o.$t("system.dmzStatus")),1)]),e("div",H,[d(u,{modelValue:v(t),"onUpdate:modelValue":s[0]||(s[0]=l=>f(t)?t.value=l:t=l)},null,8,["modelValue"])])])]),e("div",j,n(o.$t("system.dmzIP")),1),e("div",q,[d(S,{gutter:24},{default:c(()=>[d(p,{xs:24,sm:18,md:18,lg:18,xl:18},{default:c(()=>[e("div",F,[e("div",G,[d(E,{class:"inputDeep",modelValue:v(a),"onUpdate:modelValue":s[1]||(s[1]=l=>f(a)?a.value=l:a=l)},{default:c(()=>[Z("DMZ Status")]),_:1},8,["modelValue"])])])]),_:1}),d(p,{xs:6,sm:6,md:6,lg:6,xl:6},{default:c(()=>[e("div",{class:"SaveBotton",onClick:D},n(o.$t("system.save")),1)]),_:1})]),_:1})])])])])}}},_e=w(J,[["__scopeId","data-v-b3041cf8"]]);export{_e as default};