import{_ as U,X as L,U as T,k as c,F as W,o as I,g as D,s as n,p as l,w as s,q as G,z as M,a as f,x as r,c as C,Z as v,S as R}from"./index-X_y_Lweg.js";import{E as Z}from"./el-button-7z0TFEZC.js";import{E as q}from"./el-switch-KfUhZLjF.js";import{E as F}from"./el-input-CNB75vlJ.js";import{E as O,a as Q}from"./el-select-4A7gr6Mh.js";import"./el-popper-CgSMpnmi.js";import{E as X,a as j}from"./el-col-BD9sDvtY.js";import{k as H,p as w}from"./index-CcVygsTE.js";import{E as _}from"./index-r5B_-fV1.js";import{E as J}from"./index-C_xjvvip.js";import"./index-Bh4Knf-X.js";import"./index-D-QKPCnM.js";import"./use-form-item-En1b377h.js";import"./constants-C-bldqyn.js";import"./index-CQpVMKCZ.js";import"./index-r5W6hzzQ.js";import"./index-C2mSDKPv.js";import"./typescript-Bp3YSIOJ.js";import"./aria-BUADUvnR.js";import"./index-DW-jbMBa.js";import"./isEqual-DcOtnrVm.js";import"./request-CUVclgT8.js";import"./directive-ByWyyhHJ.js";const K={class:"Guide"},Y={class:"Main"},ll={class:"title"},el={class:"Smalltitle"},sl={class:"Smalltitle"},tl={class:"ItemInput"},al={class:"Smalltitle"},ol={class:"ItemInput"},nl={class:"Smalltitle"},il={class:"ItemInput"},dl={class:"Smalltitle"},ml={class:"ItemTip"},ul={class:"Smalltitle"},pl={class:"ItemInput"},rl={__name:"GuidePage",setup(gl){L();const N=T(),y=c("zhCn");let S=c("zh_CN"),i=c(""),V=c(""),g=c(""),d=c(""),m=c(!1),p=c("");W(async()=>{const t=await H({cmd:100,logon_pwd:null,logon_pwd_ignore:1});t.boot_setting=="0"&&N.push("/login"),g.value=t.wifi_name,V.value=t.wlan_signal_mode,console.log(g.value,V.value)});const P=()=>{y.value==="zhCn"?S.value="zh_CN":S.value="en_US"},k=()=>{if(d.value==""){_({message:"Please enter the WLAN password",type:"warning"}),m.value=!1;return}m.value?i.value=d.value:i.value=""},B=()=>{console.log(d.value),/^[0-9a-zA-Z]{8,16}$/.test(d.value.trim())?p.value!=""&&d.value.trim()!==p.value.trim()&&(_({message:"Two passwords are inconsistent",type:"warning"}),d.value="",p.value="",m.value&&(m.value=!1,i.value="")):(_({message:"The password format is incorrect. Please enter 8-16 digits or letters",type:"warning"}),d.value="",p.value="",m.value&&(m.value=!1,i.value=""))},A=()=>{/^[0-9a-zA-Z]{8,16}$/.test(i.value.trim())||(_({message:"The password format is incorrect. Please enter 8-16 digits or letters",type:"warning"}),i.value="")},b=async()=>{if(i.value==""||d.value==""||p.value==""){_({message:"Please fill in the content",type:"warning"});return}const a=J.service({target:"body",text:"Save  settings...",customClass:"custom-loading"});try{let t={cmd:114,logon_pwd:i.value,la:S.value},e={cmd:106,logon_pwd:"q",wifi_name:g.value,wifi_pwd:d.value,logon_pwd_ignore:1},u={cmd:123,logon_pwd:"e",changelogon_pwd:i.value,logon_pwd_ignore:1},x={cmd:133,logon_pwd:"w",logon_pwd_ignore:1},E={cmd:310,logon_pwd:"e",wlan_signal_mode:V.value,logon_pwd_ignore:1};await w(t),await w(E),await w(x),await w(u),await w(e),_({message:`Setting successful, please manually reconnect to WLAN. The new WlAN name is  ${g.value}`,type:"success",duration:8e3}),setTimeout(()=>{N.push("/login")},3e3)}catch{_({message:"Save failed",type:"warning"})}finally{a.close()}};return(a,t)=>{const e=j,u=X,x=O,E=Q,h=F,z=q,$=Z;return I(),D("div",K,[n("div",Y,[l(u,null,{default:s(()=>[l(e,{xs:24,sm:8,md:8,lg:8,xl:8}),l(e,{xs:24,sm:8,md:8,lg:8,xl:8},{default:s(()=>[n("div",ll,r(a.$t("guids.tip1")),1)]),_:1}),l(e,{xs:24,sm:8,md:8,lg:8,xl:8})]),_:1}),l(u,{class:"mt30"},{default:s(()=>[l(e,{xs:24,sm:6,md:6,lg:6,xl:6}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[n("div",el,r(a.$t("guids.tip2")),1)]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[l(E,{modelValue:y.value,"onUpdate:modelValue":t[0]||(t[0]=o=>y.value=o),placeholder:"Select",size:"large",style:{width:"240px"},onChange:P},{default:s(()=>[(I(),C(x,{key:0,label:a.$t("locale.chinese"),value:"zhCn"},null,8,["label"])),(I(),C(x,{key:1,label:a.$t("locale.english"),value:"en"},null,8,["label"]))]),_:1},8,["modelValue"])]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6})]),_:1}),l(u,{class:"mt30"},{default:s(()=>[l(e,{xs:24,sm:6,md:6,lg:6,xl:6}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[n("div",sl,r(a.$t("guids.tip3")),1)]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[n("div",tl,[l(h,{modelValue:f(g),"onUpdate:modelValue":t[1]||(t[1]=o=>v(g)?g.value=o:g=o),class:"inputDeep",placeholder:"WLAN Name",autocomplete:"off"},null,8,["modelValue"])])]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6})]),_:1}),l(u,{class:"mt30"},{default:s(()=>[l(e,{xs:24,sm:6,md:6,lg:6,xl:6}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[n("div",al,r(a.$t("guids.tip4")),1)]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[n("div",ol,[l(h,{maxlength:"16",type:"password",onBlur:B,"show-password":"",modelValue:f(d),"onUpdate:modelValue":t[2]||(t[2]=o=>v(d)?d.value=o:d=o),class:"inputDeep",placeholder:"WLAN Password",autocomplete:"off"},null,8,["modelValue"])])]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6})]),_:1}),l(u,{class:"mt30"},{default:s(()=>[l(e,{xs:24,sm:6,md:6,lg:6,xl:6}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[n("div",nl,r(a.$t("guids.tip4")),1)]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[n("div",il,[l(h,{maxlength:"16",type:"password",onBlur:B,"show-password":"",modelValue:f(p),"onUpdate:modelValue":t[3]||(t[3]=o=>v(p)?p.value=o:p=o),class:"inputDeep",placeholder:"WLAN Password",autocomplete:"off"},null,8,["modelValue"])])]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6})]),_:1}),l(u,{class:"mt30"},{default:s(()=>[l(e,{xs:24,sm:6,md:6,lg:6,xl:6}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[n("div",dl,r(a.$t("guids.tip5")),1)]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[n("div",ml,[l(z,{modelValue:f(m),"onUpdate:modelValue":t[4]||(t[4]=o=>v(m)?m.value=o:m=o),onChange:k,"active-color":"#13ce66","inactive-color":"#ff4949"},null,8,["modelValue"]),n("span",null,r(a.$t("guids.tip6")),1)])]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6})]),_:1}),G(l(u,{class:"mt30"},{default:s(()=>[l(e,{xs:24,sm:6,md:6,lg:6,xl:6}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[n("div",ul,r(a.$t("guids.tip7")),1)]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[n("div",pl,[l(h,{maxlength:"16",type:"password",modelValue:f(i),"onUpdate:modelValue":t[5]||(t[5]=o=>v(i)?i.value=o:i=o),class:"inputDeep",onBlur:A,"show-password":"",placeholder:"WLAN Password"},null,8,["modelValue"])])]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6})]),_:1},512),[[M,!f(m)]]),l(u,{class:"mt30"},{default:s(()=>[l(e,{xs:24,sm:6,md:6,lg:6,xl:6}),l(e,{xs:24,sm:12,md:12,lg:12,xl:12},{default:s(()=>[l($,{class:"SaveButton",onClick:b},{default:s(()=>[R(r(a.$t("guids.save")),1)]),_:1})]),_:1}),l(e,{xs:24,sm:6,md:6,lg:6,xl:6})]),_:1})])])}}},Tl=U(rl,[["__scopeId","data-v-2396bf92"]]);export{Tl as default};
