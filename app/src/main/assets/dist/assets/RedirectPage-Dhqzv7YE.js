import{_ as z,X as A,U as F,k as n,a1 as G,o as h,g as w,a as m,p as e,w as s,y as S,s as a,x as i,S as B,Z as b,$ as J,a0 as O}from"./index-CCLY6QsW.js";import{E as W}from"./el-switch-BTV1w9Lp.js";import{E as Y}from"./el-input-CUUfrk5p.js";import{E as Z}from"./el-button-FQWvR2f2.js";import{E as j,a as q}from"./el-col-BkR5g-BW.js";import{k as V,p as N}from"./index-CSuQBRO8.js";import{E as g}from"./index-DuTyNaIr.js";import"./index-82_0c4AB.js";import"./index-APFVxEOi.js";import"./constants-pKhjEyna.js";import"./use-form-item-BXSSCJ3O.js";import"./index-Bfki-cik.js";import"./typescript-Bp3YSIOJ.js";import"./index-vNULV4St.js";import"./index-r5W6hzzQ.js";import"./request-kOUjDz9H.js";import"./aria-BUADUvnR.js";const ee="/assets/NoSim-C9MXz0Rx.png",le=k=>(J("data-v-71852f13"),k=k(),O(),k),se={class:"RedireactPage"},te={key:0,class:"NetworkError"},ae={class:"HeadTitle"},de={class:"bodyTitle"},oe=le(()=>a("div",{class:"imgbox"},[a("img",{src:ee,alt:""})],-1)),ie={key:1,class:"pin"},ue={class:"HeadTitle"},me={class:"bodyTitle"},re={class:"Smalltitle"},ne={class:"Smalltitle"},ce={class:"ItemInput"},pe={class:"Smalltitle"},_e={key:2,class:"pukLock"},ge={key:3,class:"puk"},xe={class:"HeadTitle"},fe={class:"bodyTitle"},ve={class:"Smalltitle"},he={class:"Smalltitle"},we={class:"ItemInput"},be={class:"Smalltitle"},ke={class:"ItemInput"},ye={class:"Smalltitle"},Ie={class:"ItemInput"},Se={__name:"RedirectPage",setup(k){const{t:x}=A(),$=F();let D=n(!1),U=n(!1),f=n(!1),P=n(!1),c=n(""),T=n(),v=n(!1),y=n(),p=n(""),r=n(""),_=n(""),E={cmd:172,logon_pwd:localStorage.getItem("psd"),logon_pwd_ignore:1};G(async()=>{let t=await V(E);console.log(t),f.value=!0,y.value=t.puk_attempts_remaining,t.puk_attempts_remaining==0&&(f.value=!1,P.value=!0)});const K=()=>{if(!/^\d{4,8}$/.test(c.value)*1){g({message:x("public.redirectU"),type:"error",duration:2e3}),c.value="";return}console.log(c.value)},H=()=>{if(!/^\d{8}$/.test(p.value)){g({message:x("public.redirectV"),type:"error",duration:2e3}),p.value="";return}console.log(p.value)},R=()=>{if(/^\d{4,8}$/.test(r.value)){if(_.value!=""&&r.value!=_.value){g({message:x("public.redirectW"),type:"error",duration:2e3}),r.value="",_.value="";return}}else{g({message:x("public.redirectU"),type:"error",duration:2e3}),r.value="",_.value="";return}},M=async()=>{if(!c.value){g({message:x("public.redirectX"),type:"error",duration:2e3});return}let t={cmd:143,pin:c.value,logon_pwd:"123",logon_pwd_ignore:1,ver:"1"},d=await N(t);if(d.code==200)if(d.sim_pin_result.success==1){if(v.value==!0){let l={cmd:142,pin:c.value,enabled:0,logon_pwd:"123",logon_pwd_ignore:1,ver:"1"};await N(l)}g({message:"Success",type:"success",duration:2e3}),setTimeout(()=>{$.push("/login")},5e3)}else{g({message:"failure",type:"error",duration:2e3}),setTimeout(()=>{X()},500);let l=await V(E);T.value=l.pin_attempts_remaining}};async function X(){(await V({cmd:145})).status==3&&(f.value=!0,U.value=!1,y.value=10)}const L=async()=>{if(p.value==""||r.value==""||_.value==""){g({message:x("public.redirectY"),type:"error",duration:2e3});return}let t={cmd:149,pin:r.value,puk:p.value,logon_pwd:"111",logon_pwd_ignore:1,ver:"1"},d=await N(t);if(d.code==200)if(d.sim_pin_result.success==1)g({message:"Success",type:"success",duration:2e3}),setTimeout(()=>{$.push("/login")},5e3);else{r.value="",_.value="",p.value="",g({message:"failure",type:"error",duration:2e3});let l=await V(E);y.value=l.puk_attempts_remaining,l.puk_attempts_remaining==0&&(f.value=!1,P.value=!0)}};return(t,d)=>{const l=q,o=j,C=Z,I=Y,Q=W;return h(),w("div",se,[m(D)?(h(),w("div",te,[e(o,null,{default:s(()=>[e(l,{xs:24,sm:24,md:24,lg:24,xl:24},{default:s(()=>[a("div",ae,i(t.$t("public.redirectA")),1)]),_:1})]),_:1}),e(o,null,{default:s(()=>[e(l,{xs:24,sm:24,md:24,lg:24,xl:24},{default:s(()=>[a("div",de,i(t.$t("public.redirectB")),1)]),_:1})]),_:1}),e(o,null,{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:8,md:8,lg:8,xl:8},{default:s(()=>[oe]),_:1}),e(l,{xs:24,sm:8,md:8,lg:8,xl:8})]),_:1}),e(o,{class:"pd20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:8,md:8,lg:8,xl:8},{default:s(()=>[e(C,{class:"backbutton",onClick:d[0]||(d[0]=u=>m($).push("/login"))},{default:s(()=>[B(i(t.$t("public.redirectC")),1)]),_:1})]),_:1}),e(l,{xs:24,sm:8,md:8,lg:8,xl:8})]),_:1})])):S("",!0),m(U)?(h(),w("div",ie,[e(o,null,{default:s(()=>[e(l,{xs:24,sm:24,md:24,lg:24,xl:24},{default:s(()=>[a("div",ue,i(t.$t("public.redirectD")),1)]),_:1})]),_:1}),e(o,null,{default:s(()=>[e(l,{xs:24,sm:24,md:24,lg:24,xl:24},{default:s(()=>[a("div",me,i(t.$t("public.redirectE")),1)]),_:1})]),_:1}),e(o,{class:"mt20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:3,md:3,lg:3,xl:3},{default:s(()=>[a("div",re,i(t.$t("public.redirectF")),1)]),_:1}),e(l,{xs:24,sm:6,md:6,lg:6,xl:6}),e(l,{xs:24,sm:7,md:7,lg:7,xl:7})]),_:1}),e(o,{class:"mt20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:3,md:3,lg:3,xl:3},{default:s(()=>[a("div",ne,i(t.$t("public.redirectG")),1)]),_:1}),e(l,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[a("div",ce,[e(I,{maxlength:"8",type:"password",modelValue:m(c),"onUpdate:modelValue":d[1]||(d[1]=u=>b(c)?c.value=u:c=u),class:"inputDeep",onBlur:K,"show-password":"",placeholder:t.$t("public.redirectH")},null,8,["modelValue","placeholder"])])]),_:1}),e(l,{xs:24,sm:7,md:7,lg:7,xl:7})]),_:1}),e(o,{class:"mt20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:3,md:3,lg:3,xl:3}),e(l,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[a("div",null,i(t.$t("public.redirectI"))+i(m(T)),1)]),_:1}),e(l,{xs:24,sm:7,md:7,lg:7,xl:7})]),_:1}),e(o,{class:"mt20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:3,md:3,lg:3,xl:3},{default:s(()=>[a("div",pe,i(t.$t("public.redirectJ")),1)]),_:1}),e(l,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[e(Q,{modelValue:m(v),"onUpdate:modelValue":d[2]||(d[2]=u=>b(v)?v.value=u:v=u),"active-color":"#13ce66","inactive-color":"#ff4949"},null,8,["modelValue"])]),_:1}),e(l,{xs:24,sm:7,md:7,lg:7,xl:7})]),_:1}),e(o,{class:"mt20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:8,md:8,lg:8,xl:8},{default:s(()=>[e(C,{class:"SaveButton",onClick:M},{default:s(()=>[B("save")]),_:1})]),_:1}),e(l,{xs:24,sm:8,md:8,lg:8,xl:8})]),_:1})])):S("",!0),m(P)?(h(),w("div",_e)):S("",!0),m(f)?(h(),w("div",ge,[e(o,null,{default:s(()=>[e(l,{xs:24,sm:24,md:24,lg:24,xl:24},{default:s(()=>[a("div",xe,i(t.$t("public.redirectK")),1)]),_:1})]),_:1}),e(o,null,{default:s(()=>[e(l,{xs:24,sm:24,md:24,lg:24,xl:24},{default:s(()=>[a("div",fe,i(t.$t("public.redirectL")),1)]),_:1})]),_:1}),e(o,{class:"mt20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:3,md:3,lg:3,xl:3},{default:s(()=>[a("div",ve,i(t.$t("public.redirectM")),1)]),_:1}),e(l,{xs:24,sm:6,md:6,lg:6,xl:6}),e(l,{xs:24,sm:7,md:7,lg:7,xl:7})]),_:1}),e(o,{class:"mt20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:3,md:3,lg:3,xl:3},{default:s(()=>[a("div",he,i(t.$t("public.redirectN")),1)]),_:1}),e(l,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[a("div",we,[e(I,{maxlength:"8",type:"password",modelValue:m(p),"onUpdate:modelValue":d[3]||(d[3]=u=>b(p)?p.value=u:p=u),class:"inputDeep",onBlur:H,"show-password":"",placeholder:t.$t("public.redirectO")},null,8,["modelValue","placeholder"])])]),_:1}),e(l,{xs:24,sm:7,md:7,lg:7,xl:7})]),_:1}),e(o,{class:"mt20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:3,md:3,lg:3,xl:3},{default:s(()=>[a("div",be,i(t.$t("public.redirectP")),1)]),_:1}),e(l,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[a("div",ke,[e(I,{maxlength:"8",type:"password",modelValue:m(r),"onUpdate:modelValue":d[4]||(d[4]=u=>b(r)?r.value=u:r=u),class:"inputDeep",onBlur:R,"show-password":"",placeholder:t.$t("public.redirectQ")},null,8,["modelValue","placeholder"])])]),_:1}),e(l,{xs:24,sm:7,md:7,lg:7,xl:7})]),_:1}),e(o,{class:"mt20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:3,md:3,lg:3,xl:3},{default:s(()=>[a("div",ye,i(t.$t("public.redirectS")),1)]),_:1}),e(l,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[a("div",Ie,[e(I,{maxlength:"8",type:"password",modelValue:m(_),"onUpdate:modelValue":d[5]||(d[5]=u=>b(_)?_.value=u:_=u),class:"inputDeep",onBlur:R,"show-password":"",placeholder:t.$t("public.redirectS")},null,8,["modelValue","placeholder"])])]),_:1}),e(l,{xs:24,sm:7,md:7,lg:7,xl:7})]),_:1}),e(o,{class:"mt20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:2,md:2,lg:2,xl:2}),e(l,{xs:24,sm:6,md:6,lg:6,xl:6},{default:s(()=>[a("div",null,i(t.$t("public.redirectI"))+":"+i(m(y)),1)]),_:1}),e(l,{xs:24,sm:8,md:8,lg:8,xl:8})]),_:1}),e(o,{class:"mt20"},{default:s(()=>[e(l,{xs:24,sm:8,md:8,lg:8,xl:8}),e(l,{xs:24,sm:8,md:8,lg:8,xl:8},{default:s(()=>[e(C,{class:"SaveButton",onClick:L},{default:s(()=>[B("save")]),_:1})]),_:1}),e(l,{xs:24,sm:8,md:8,lg:8,xl:8})]),_:1})])):S("",!0)])}}},ze=z(Se,[["__scopeId","data-v-71852f13"]]);export{ze as default};