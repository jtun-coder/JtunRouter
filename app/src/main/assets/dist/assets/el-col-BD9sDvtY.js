import{j as g,d as f,u as _,f as o,E as R,o as $,c as h,w,r as v,n as j,a as c,h as N,a3 as x,b as C,M as r,B as S,al as b,N as K}from"./index-X_y_Lweg.js";import{_ as E}from"./index-Bh4Knf-X.js";import{m as u}from"./typescript-Bp3YSIOJ.js";const O=Symbol("rowContextKey"),B=["start","center","end","space-around","space-between","space-evenly"],P=["top","middle","bottom"],k=g({tag:{type:String,default:"div"},gutter:{type:Number,default:0},justify:{type:String,values:B,default:"start"},align:{type:String,values:P}}),L=f({name:"ElRow"}),A=f({...L,props:k,setup(p){const e=p,l=_("row"),a=o(()=>e.gutter);R(O,{gutter:a});const i=o(()=>{const t={};return e.gutter&&(t.marginRight=t.marginLeft=`-${e.gutter/2}px`),t}),d=o(()=>[l.b(),l.is(`justify-${e.justify}`,e.justify!=="start"),l.is(`align-${e.align}`,!!e.align)]);return(t,m)=>($(),h(x(t.tag),{class:j(c(d)),style:N(c(i))},{default:w(()=>[v(t.$slots,"default")]),_:3},8,["class","style"]))}});var D=E(A,[["__file","row.vue"]]);const Q=C(D),I=g({tag:{type:String,default:"div"},span:{type:Number,default:24},offset:{type:Number,default:0},pull:{type:Number,default:0},push:{type:Number,default:0},xs:{type:r([Number,Object]),default:()=>u({})},sm:{type:r([Number,Object]),default:()=>u({})},md:{type:r([Number,Object]),default:()=>u({})},lg:{type:r([Number,Object]),default:()=>u({})},xl:{type:r([Number,Object]),default:()=>u({})}}),J=f({name:"ElCol"}),M=f({...J,props:I,setup(p){const e=p,{gutter:l}=S(O,{gutter:o(()=>0)}),a=_("col"),i=o(()=>{const t={};return l.value&&(t.paddingLeft=t.paddingRight=`${l.value/2}px`),t}),d=o(()=>{const t=[];return["span","offset","pull","push"].forEach(s=>{const n=e[s];b(n)&&(s==="span"?t.push(a.b(`${e[s]}`)):n>0&&t.push(a.b(`${s}-${e[s]}`)))}),["xs","sm","md","lg","xl"].forEach(s=>{b(e[s])?t.push(a.b(`${s}-${e[s]}`)):K(e[s])&&Object.entries(e[s]).forEach(([n,y])=>{t.push(n!=="span"?a.b(`${s}-${n}-${y}`):a.b(`${s}-${y}`))})}),l.value&&t.push(a.is("guttered")),[a.b(),t]});return(t,m)=>($(),h(x(t.tag),{class:j(c(d)),style:N(c(i))},{default:w(()=>[v(t.$slots,"default")]),_:3},8,["class","style"]))}});var T=E(M,[["__file","col.vue"]]);const U=C(T);export{Q as E,U as a};
