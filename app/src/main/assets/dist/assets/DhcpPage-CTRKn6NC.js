import{_ as R,U as $,a4 as F,k as _,C as H,a1 as q,o as V,g as y,s as e,x as r,p as s,w as a,q as B,z as E,a as d,Z as P,J as z,Y as J,S as O,$ as Y,a0 as Z}from"./index-CCLY6QsW.js";import{E as j}from"./el-button-FQWvR2f2.js";import{E as G,a as K}from"./el-col-BkR5g-BW.js";import{E as Q}from"./el-input-CUUfrk5p.js";import{O as W}from"./openDownLoad-DhbK9y2K.js";import{p as A}from"./index-CSuQBRO8.js";import{E as b}from"./index-DuTyNaIr.js";import"./index-82_0c4AB.js";import"./index-vNULV4St.js";import"./use-form-item-BXSSCJ3O.js";import"./constants-pKhjEyna.js";import"./index-APFVxEOi.js";import"./index-r5W6hzzQ.js";import"./typescript-Bp3YSIOJ.js";import"./index-Bfki-cik.js";import"./request-kOUjDz9H.js";import"./aria-BUADUvnR.js";const X=I=>(Y("data-v-e3dacbeb"),I=I(),Z(),I),ee={class:"OnlineClient"},se={class:"OnlineClientTop"},le={class:"OnlineHeader"},te={class:"headerLeft"},ae={class:"OnlineBody"},oe={class:"setItem"},ne={class:"setItemName"},de={class:"setItemValue"},ue={class:"setItem"},me={class:"setItemName"},ce={class:"setItemValue"},ie=X(()=>e("div",{class:"setItemLine"},"——",-1)),re={class:"setItemValue"},_e={class:"setItem"},pe={class:"setItemName"},ge={class:"setItemValue"},ve={class:"dropdownBlock"},fe={class:"dropApnLeft"},he={class:"disAble"},De=["src"],Ie={class:"drowdownBottom"},Ce=["onClick"],we={class:"setItem"},xe={class:"setItemValue"},Ve={class:"setItem"},ye={class:"SaveBlock"},Pe={__name:"DhcpPage",setup(I){const S=$(),c=F();let C=_(""),p=_(""),f=_(""),g=H({rangA:"",rangB:""}),i=H({name:"",value:""}),h=_(!1);const k=[{name:"1 Hours",value:1},{name:"12 Hours",value:12},{name:"One Day",value:24},{name:"One week",value:168},{name:"custom",value:"custom"}];let w=_(null),x=_(!1),v=_();q(()=>{p.value=c.globalData.gateway,C.value=c.globalData.logon_pwd,f.value=c.globalData.mask,g.rangA=c.globalData.dhcp_start,g.rangB=c.globalData.dhcp_end,k.find(l=>{l.value==c.globalData.dhcp_lease/3600?i=l:i={name:c.globalData.dhcp_lease/3600+"hours",value:c.globalData.dhcp_lease/3600}}),console.log(i)});const T=()=>{h.value=!h.value},U=l=>{l.value=="custom"?(i=l,h.value=!1,x.value=!0,w.value.focus()):(x.value=!1,i=l,h.value=!1)},L=(l,o)=>{console.log(l);const n=A(l),u=A(o);Promise.all([n,u]).then(([m,D])=>{console.log(m,D),b({message:"The device is restarting. Please wait for a while before reconnecting",type:"success",duration:3e3}),setTimeout(()=>{let t=localStorage.getItem("lang")||"en";localStorage.clear(),localStorage.setItem("lang",t),S.push("/login")},3e3)}).catch(([m,D])=>{console.log(m,D),setTimeout(()=>{b({message:"Device setup failed",type:"error",duration:3e3});let t=localStorage.getItem("lang")||"en";localStorage.clear(),localStorage.setItem("lang",t),S.push("/login")},3e3)})},N=()=>{const l=i.value==="custom",o=l?Math.floor(v.value):i.value;if(l&&(o<1||o>168)){b({message:"Please enter an integer between 1-168",type:"error"}),v.value=1,w.value.focus();return}const n={cmd:127,logon_pwd:C.value,gateway:p.value},u={cmd:219,logon_pwd:C.value,dhcp_lease:o*3600+""};console.log(u),L(u,n)};return(l,o)=>{const n=K,u=Q,m=G,D=j;return V(),y("div",null,[e("div",ee,[e("div",se,[e("div",le,[e("div",te,r(l.$t("system.dhcp")),1)]),e("div",ae,[e("div",oe,[s(m,null,{default:a(()=>[s(n,{xs:24,sm:5,md:5,lg:5,xl:5},{default:a(()=>[e("div",ne,r(l.$t("system.lan")),1)]),_:1}),s(n,{xs:24,sm:19,md:19,lg:19,xl:19},{default:a(()=>[e("div",de,[s(u,{readonly:"",class:"inputDeep",modelValue:d(p),"onUpdate:modelValue":o[0]||(o[0]=t=>P(p)?p.value=t:p=t)},null,8,["modelValue"])])]),_:1})]),_:1})]),e("div",ue,[s(m,null,{default:a(()=>[s(n,{xs:24,sm:5,md:5,lg:5,xl:5},{default:a(()=>[e("div",me,r(l.$t("system.mask")),1)]),_:1}),s(n,{xs:24,sm:9,md:9,lg:9,xl:9},{default:a(()=>[e("div",ce,[s(u,{readonly:"",class:"inputDeep",modelValue:d(g).rangA,"onUpdate:modelValue":o[1]||(o[1]=t=>d(g).rangA=t)},null,8,["modelValue"])])]),_:1}),s(n,{xs:24,sm:1,md:1,lg:1,xl:1},{default:a(()=>[ie]),_:1}),s(n,{xs:24,sm:9,md:9,lg:9,xl:9},{default:a(()=>[e("div",re,[s(u,{readonly:"",class:"inputDeep",modelValue:d(g).rangB,"onUpdate:modelValue":o[2]||(o[2]=t=>d(g).rangB=t)},null,8,["modelValue"])])]),_:1})]),_:1})]),e("div",_e,[s(m,null,{default:a(()=>[s(n,{xs:24,sm:5,md:5,lg:5,xl:5},{default:a(()=>[e("div",pe,r(l.$t("system.range")),1)]),_:1}),s(n,{xs:24,sm:19,md:19,lg:19,xl:19},{default:a(()=>[e("div",ge,[s(u,{readonly:"",class:"inputDeep",modelValue:d(f),"onUpdate:modelValue":o[3]||(o[3]=t=>P(f)?f.value=t:f=t)},null,8,["modelValue"])])]),_:1})]),_:1})]),e("div",ve,[s(m,null,{default:a(()=>[s(n,{xs:24,sm:5,md:5,lg:5,xl:5},{default:a(()=>[e("div",fe,r(l.$t("system.lease")),1)]),_:1}),s(n,{xs:24,sm:19,md:19,lg:19,xl:19},{default:a(()=>[(V(),y("div",{class:"dropApnRight",key:Math.random()},[e("div",{class:"dropdownUp",onClick:T},[e("div",he,r(d(i).name),1),e("img",{class:"OpenDrow",src:d(W),alt:""},null,8,De)]),B(e("div",Ie,[(V(),y(z,null,J(k,(t,M)=>e("div",{key:M,class:"drowdownItem",onClick:be=>U(t)},r(t.name),9,Ce)),64))],512),[[E,d(h)]])]))]),_:1})]),_:1})]),B(e("div",we,[s(m,null,{default:a(()=>[s(n,{xs:24,sm:5,md:5,lg:5,xl:5}),s(n,{xs:24,sm:19,md:19,lg:19,xl:19},{default:a(()=>[e("div",xe,[s(u,{ref_key:"CustomDHCPInput",ref:w,class:"inputDeep",type:"number",autofocus:!0,modelValue:d(v),"onUpdate:modelValue":o[4]||(o[4]=t=>P(v)?v.value=t:v=t)},null,8,["modelValue"]),O("Hours ")])]),_:1})]),_:1})],512),[[E,d(x)]]),e("div",Ve,[e("div",ye,[s(D,{class:"DhcpSave",onClick:N},{default:a(()=>[O(r(l.$t("system.save")),1)]),_:1})])])])])])])}}},Je=R(Pe,[["__scopeId","data-v-e3dacbeb"]]);export{Je as default};