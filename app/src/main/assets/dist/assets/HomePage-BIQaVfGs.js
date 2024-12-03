import{d as T,u as H,o as g,c as K,w as k,r as B,m as Ee,t as nt,a,T as $e,b as be,e as ze,f as y,g as $,n as E,h as Se,i as ee,j as ye,k as x,l as ot,p as A,q as re,s as C,v as st,x as Z,y as J,z as ve,A as Te,B as he,C as Pe,D as ie,E as Ie,F as ce,G as xe,H as O,I as ne,J as oe,K as Be,L as at,M as ge,N as lt,O as He,P as it,Q as rt,R as Re,S as ue,_ as Ne,U as Fe,V as ut,W as De,X as ct,Y as Le,Z as dt,$ as Ve,a0 as We,a1 as pt}from"./index-CCLY6QsW.js";import{E as je,C as ft}from"./el-popper-DEnoFQL7.js";import{u as mt}from"./index-Bfki-cik.js";import{_ as W,a as vt,E as Oe,c as ht,b as fe,r as Me,h as gt,d as _t,e as bt,m as yt}from"./index-82_0c4AB.js";import{E as V}from"./aria-BUADUvnR.js";import{T as Mt}from"./index-r5W6hzzQ.js";import{t as _e,i as le}from"./index-APFVxEOi.js";import{m as wt}from"./typescript-Bp3YSIOJ.js";import{d as Ct,a as kt,u as It,E as Et,f as $t}from"./el-overlay-89ZHhflZ.js";import{E as St,a as Tt}from"./index-z4wGpwji.js";import{u as Pt}from"./index-vNULV4St.js";import"./constants-pKhjEyna.js";const me=function(e,o,...n){let s;o.includes("mouse")||o.includes("click")?s="MouseEvents":o.includes("key")?s="KeyboardEvent":s="HTMLEvents";const t=document.createEvent(s);return t.initEvent(o,...n),e.dispatchEvent(t),e},xt=T({name:"ElCollapseTransition"}),Nt=T({...xt,setup(e){const o=H("collapse-transition"),n=t=>{t.style.maxHeight="",t.style.overflow=t.dataset.oldOverflow,t.style.paddingTop=t.dataset.oldPaddingTop,t.style.paddingBottom=t.dataset.oldPaddingBottom},s={beforeEnter(t){t.dataset||(t.dataset={}),t.dataset.oldPaddingTop=t.style.paddingTop,t.dataset.oldPaddingBottom=t.style.paddingBottom,t.style.height&&(t.dataset.elExistsHeight=t.style.height),t.style.maxHeight=0,t.style.paddingTop=0,t.style.paddingBottom=0},enter(t){requestAnimationFrame(()=>{t.dataset.oldOverflow=t.style.overflow,t.dataset.elExistsHeight?t.style.maxHeight=t.dataset.elExistsHeight:t.scrollHeight!==0?t.style.maxHeight=`${t.scrollHeight}px`:t.style.maxHeight=0,t.style.paddingTop=t.dataset.oldPaddingTop,t.style.paddingBottom=t.dataset.oldPaddingBottom,t.style.overflow="hidden"})},afterEnter(t){t.style.maxHeight="",t.style.overflow=t.dataset.oldOverflow},enterCancelled(t){n(t)},beforeLeave(t){t.dataset||(t.dataset={}),t.dataset.oldPaddingTop=t.style.paddingTop,t.dataset.oldPaddingBottom=t.style.paddingBottom,t.dataset.oldOverflow=t.style.overflow,t.style.maxHeight=`${t.scrollHeight}px`,t.style.overflow="hidden"},leave(t){t.scrollHeight!==0&&(t.style.maxHeight=0,t.style.paddingTop=0,t.style.paddingBottom=0)},afterLeave(t){n(t)},leaveCancelled(t){n(t)}};return(t,v)=>(g(),K($e,Ee({name:a(o).b()},nt(s)),{default:k(()=>[B(t.$slots,"default")]),_:3},16,["name"]))}});var Ot=W(Nt,[["__file","collapse-transition.vue"]]);const At=be(Ot),Bt=T({name:"ElContainer"}),Ht=T({...Bt,props:{direction:{type:String}},setup(e){const o=e,n=ze(),s=H("container"),t=y(()=>o.direction==="vertical"?!0:o.direction==="horizontal"?!1:n&&n.default?n.default().some(r=>{const h=r.type.name;return h==="ElHeader"||h==="ElFooter"}):!1);return(v,r)=>(g(),$("section",{class:E([a(s).b(),a(s).is("vertical",a(t))])},[B(v.$slots,"default")],2))}});var Lt=W(Ht,[["__file","container.vue"]]);const zt=T({name:"ElAside"}),Rt=T({...zt,props:{width:{type:String,default:null}},setup(e){const o=e,n=H("aside"),s=y(()=>o.width?n.cssVarBlock({width:o.width}):{});return(t,v)=>(g(),$("aside",{class:E(a(n).b()),style:Se(a(s))},[B(t.$slots,"default")],6))}});var qe=W(Rt,[["__file","aside.vue"]]);const Ft=T({name:"ElFooter"}),Dt=T({...Ft,props:{height:{type:String,default:null}},setup(e){const o=e,n=H("footer"),s=y(()=>o.height?n.cssVarBlock({height:o.height}):{});return(t,v)=>(g(),$("footer",{class:E(a(n).b()),style:Se(a(s))},[B(t.$slots,"default")],6))}});var Ue=W(Dt,[["__file","footer.vue"]]);const Vt=T({name:"ElHeader"}),Wt=T({...Vt,props:{height:{type:String,default:null}},setup(e){const o=e,n=H("header"),s=y(()=>o.height?n.cssVarBlock({height:o.height}):{});return(t,v)=>(g(),$("header",{class:E(a(n).b()),style:Se(a(s))},[B(t.$slots,"default")],6))}});var Ge=W(Wt,[["__file","header.vue"]]);const jt=T({name:"ElMain"}),qt=T({...jt,setup(e){const o=H("main");return(n,s)=>(g(),$("main",{class:E(a(o).b())},[B(n.$slots,"default")],2))}});var Je=W(qt,[["__file","main.vue"]]);const Ut=be(Lt,{Aside:qe,Footer:Ue,Header:Ge,Main:Je}),Ke=ee(qe),Gt=ee(Ue),Jt=ee(Ge),Kt=ee(Je),Zt=ye({...Ct,direction:{type:String,default:"rtl",values:["ltr","rtl","ttb","btt"]},size:{type:[String,Number],default:"30%"},withHeader:{type:Boolean,default:!0},modalFade:{type:Boolean,default:!0},headerAriaLevel:{type:String,default:"2"}}),Qt=kt,Xt=T({name:"ElDrawer",inheritAttrs:!1}),Yt=T({...Xt,props:Zt,emits:Qt,setup(e,{expose:o}){const n=e,s=ze();Pt({scope:"el-drawer",from:"the title slot",replacement:"the header slot",version:"3.0.0",ref:"https://element-plus.org/en-US/component/drawer.html#slots"},y(()=>!!s.title));const t=x(),v=x(),r=H("drawer"),{t:h}=ot(),{afterEnter:i,afterLeave:p,beforeLeave:u,visible:m,rendered:_,titleId:S,bodyId:U,zIndex:G,onModalClick:w,onOpenAutoFocus:L,onCloseAutoFocus:F,onFocusoutPrevented:Q,onCloseRequested:se,handleClose:X}=It(n,t),R=y(()=>n.direction==="rtl"||n.direction==="ltr"),j=y(()=>vt(n.size));return o({handleClose:X,afterEnter:i,afterLeave:p}),(b,Y)=>(g(),K(a(Tt),{to:b.appendTo,disabled:b.appendTo!=="body"?!1:!b.appendToBody},{default:k(()=>[A($e,{name:a(r).b("fade"),onAfterEnter:a(i),onAfterLeave:a(p),onBeforeLeave:a(u),persisted:""},{default:k(()=>[re(A(a(Et),{mask:b.modal,"overlay-class":b.modalClass,"z-index":a(G),onClick:a(w)},{default:k(()=>[A(a(St),{loop:"",trapped:a(m),"focus-trap-el":t.value,"focus-start-el":v.value,onFocusAfterTrapped:a(L),onFocusAfterReleased:a(F),onFocusoutPrevented:a(Q),onReleaseRequested:a(se)},{default:k(()=>[C("div",Ee({ref_key:"drawerRef",ref:t,"aria-modal":"true","aria-label":b.title||void 0,"aria-labelledby":b.title?void 0:a(S),"aria-describedby":a(U)},b.$attrs,{class:[a(r).b(),b.direction,a(m)&&"open"],style:a(R)?"width: "+a(j):"height: "+a(j),role:"dialog",onClick:st(()=>{},["stop"])}),[C("span",{ref_key:"focusStartRef",ref:v,class:E(a(r).e("sr-focus")),tabindex:"-1"},null,2),b.withHeader?(g(),$("header",{key:0,class:E(a(r).e("header"))},[b.$slots.title?B(b.$slots,"title",{key:1},()=>[J(" DEPRECATED SLOT ")]):B(b.$slots,"header",{key:0,close:a(X),titleId:a(S),titleClass:a(r).e("title")},()=>[b.$slots.title?J("v-if",!0):(g(),$("span",{key:0,id:a(S),role:"heading","aria-level":b.headerAriaLevel,class:E(a(r).e("title"))},Z(b.title),11,["id","aria-level"]))]),b.showClose?(g(),$("button",{key:2,"aria-label":a(h)("el.drawer.close"),class:E(a(r).e("close-btn")),type:"button",onClick:a(X)},[A(a(Oe),{class:E(a(r).e("close"))},{default:k(()=>[A(a(ht))]),_:1},8,["class"])],10,["aria-label","onClick"])):J("v-if",!0)],2)):J("v-if",!0),a(_)?(g(),$("div",{key:1,id:a(U),class:E(a(r).e("body"))},[B(b.$slots,"default")],10,["id"])):J("v-if",!0),b.$slots.footer?(g(),$("div",{key:2,class:E(a(r).e("footer"))},[B(b.$slots,"footer")],2)):J("v-if",!0)],16,["aria-label","aria-labelledby","aria-describedby","onClick"])]),_:3},8,["trapped","focus-trap-el","focus-start-el","onFocusAfterTrapped","onFocusAfterReleased","onFocusoutPrevented","onReleaseRequested"])]),_:3},8,["mask","overlay-class","z-index","onClick"]),[[ve,a(m)]])]),_:3},8,["name","onAfterEnter","onAfterLeave","onBeforeLeave"])]),_:3},8,["to","disabled"]))}});var en=W(Yt,[["__file","drawer.vue"]]);const tn=be(en);let nn=class{constructor(o,n){this.parent=o,this.domNode=n,this.subIndex=0,this.subIndex=0,this.init()}init(){this.subMenuItems=this.domNode.querySelectorAll("li"),this.addListeners()}gotoSubIndex(o){o===this.subMenuItems.length?o=0:o<0&&(o=this.subMenuItems.length-1),this.subMenuItems[o].focus(),this.subIndex=o}addListeners(){const o=this.parent.domNode;Array.prototype.forEach.call(this.subMenuItems,n=>{n.addEventListener("keydown",s=>{let t=!1;switch(s.code){case V.down:{this.gotoSubIndex(this.subIndex+1),t=!0;break}case V.up:{this.gotoSubIndex(this.subIndex-1),t=!0;break}case V.tab:{me(o,"mouseleave");break}case V.enter:case V.space:{t=!0,s.currentTarget.click();break}}return t&&(s.preventDefault(),s.stopPropagation()),!1})})}},on=class{constructor(o,n){this.domNode=o,this.submenu=null,this.submenu=null,this.init(n)}init(o){this.domNode.setAttribute("tabindex","0");const n=this.domNode.querySelector(`.${o}-menu`);n&&(this.submenu=new nn(this,n)),this.addListeners()}addListeners(){this.domNode.addEventListener("keydown",o=>{let n=!1;switch(o.code){case V.down:{me(o.currentTarget,"mouseenter"),this.submenu&&this.submenu.gotoSubIndex(0),n=!0;break}case V.up:{me(o.currentTarget,"mouseenter"),this.submenu&&this.submenu.gotoSubIndex(this.submenu.subMenuItems.length-1),n=!0;break}case V.tab:{me(o.currentTarget,"mouseleave");break}case V.enter:case V.space:{n=!0,o.currentTarget.click();break}}n&&o.preventDefault()})}},sn=class{constructor(o,n){this.domNode=o,this.init(n)}init(o){const n=this.domNode.childNodes;Array.from(n).forEach(s=>{s.nodeType===1&&new on(s,o)})}};const an=T({name:"ElMenuCollapseTransition",setup(){const e=H("menu");return{listeners:{onBeforeEnter:n=>n.style.opacity="0.2",onEnter(n,s){fe(n,`${e.namespace.value}-opacity-transition`),n.style.opacity="1",s()},onAfterEnter(n){Me(n,`${e.namespace.value}-opacity-transition`),n.style.opacity=""},onBeforeLeave(n){n.dataset||(n.dataset={}),gt(n,e.m("collapse"))?(Me(n,e.m("collapse")),n.dataset.oldOverflow=n.style.overflow,n.dataset.scrollWidth=n.clientWidth.toString(),fe(n,e.m("collapse"))):(fe(n,e.m("collapse")),n.dataset.oldOverflow=n.style.overflow,n.dataset.scrollWidth=n.clientWidth.toString(),Me(n,e.m("collapse"))),n.style.width=`${n.scrollWidth}px`,n.style.overflow="hidden"},onLeave(n){fe(n,"horizontal-collapse-transition"),n.style.width=`${n.dataset.scrollWidth}px`}}}}});function ln(e,o,n,s,t,v){return g(),K($e,Ee({mode:"out-in"},e.listeners),{default:k(()=>[B(e.$slots,"default")]),_:3},16)}var rn=W(an,[["render",ln],["__file","menu-collapse-transition.vue"]]);function Ze(e,o){const n=y(()=>{let t=e.parent;const v=[o.value];for(;t.type.name!=="ElMenu";)t.props.index&&v.unshift(t.props.index),t=t.parent;return v});return{parentMenu:y(()=>{let t=e.parent;for(;t&&!["ElMenu","ElSubMenu"].includes(t.type.name);)t=t.parent;return t}),indexPath:n}}function un(e){return y(()=>{const n=e.backgroundColor;return n?new Mt(n).shade(20).toString():""})}const Qe=(e,o)=>{const n=H("menu");return y(()=>n.cssVarBlock({"text-color":e.textColor||"","hover-text-color":e.textColor||"","bg-color":e.backgroundColor||"","hover-bg-color":un(e).value||"","active-color":e.activeTextColor||"",level:`${o}`}))},cn=ye({index:{type:String,required:!0},showTimeout:Number,hideTimeout:Number,popperClass:String,disabled:Boolean,teleported:{type:Boolean,default:void 0},popperOffset:Number,expandCloseIcon:{type:le},expandOpenIcon:{type:le},collapseCloseIcon:{type:le},collapseOpenIcon:{type:le}}),we="ElSubMenu";var Ae=T({name:we,props:cn,setup(e,{slots:o,expose:n}){const s=Te(),{indexPath:t,parentMenu:v}=Ze(s,y(()=>e.index)),r=H("menu"),h=H("sub-menu"),i=he("rootMenu");i||_e(we,"can not inject root menu");const p=he(`subMenu:${v.value.uid}`);p||_e(we,"can not inject sub menu");const u=x({}),m=x({});let _;const S=x(!1),U=x(),G=x(null),w=y(()=>b.value==="horizontal"&&F.value?"bottom-start":"right-start"),L=y(()=>b.value==="horizontal"&&F.value||b.value==="vertical"&&!i.props.collapse?e.expandCloseIcon&&e.expandOpenIcon?R.value?e.expandOpenIcon:e.expandCloseIcon:_t:e.collapseCloseIcon&&e.collapseOpenIcon?R.value?e.collapseOpenIcon:e.collapseCloseIcon:bt),F=y(()=>p.level===0),Q=y(()=>{const d=e.teleported;return d===void 0?F.value:d}),se=y(()=>i.props.collapse?`${r.namespace.value}-zoom-in-left`:`${r.namespace.value}-zoom-in-top`),X=y(()=>b.value==="horizontal"&&F.value?["bottom-start","bottom-end","top-start","top-end","right-start","left-start"]:["right-start","right","right-end","left-start","bottom-start","bottom-end","top-start","top-end"]),R=y(()=>i.openedMenus.includes(e.index)),j=y(()=>{let d=!1;return Object.values(u.value).forEach(M=>{M.active&&(d=!0)}),Object.values(m.value).forEach(M=>{M.active&&(d=!0)}),d}),b=y(()=>i.props.mode),Y=Pe({index:e.index,indexPath:t,active:j}),ae=Qe(i.props,p.level+1),de=y(()=>{var d;return(d=e.popperOffset)!=null?d:i.props.popperOffset}),te=y(()=>{var d;return(d=e.popperClass)!=null?d:i.props.popperClass}),pe=y(()=>{var d;return(d=e.showTimeout)!=null?d:i.props.showTimeout}),l=y(()=>{var d;return(d=e.hideTimeout)!=null?d:i.props.hideTimeout}),c=()=>{var d,M,N;return(N=(M=(d=G.value)==null?void 0:d.popperRef)==null?void 0:M.popperInstanceRef)==null?void 0:N.destroy()},f=d=>{d||c()},P=()=>{i.props.menuTrigger==="hover"&&i.props.mode==="horizontal"||i.props.collapse&&i.props.mode==="vertical"||e.disabled||i.handleSubMenuClick({index:e.index,indexPath:t.value,active:j.value})},I=(d,M=pe.value)=>{var N;if(d.type!=="focus"){if(i.props.menuTrigger==="click"&&i.props.mode==="horizontal"||!i.props.collapse&&i.props.mode==="vertical"||e.disabled){p.mouseInChild.value=!0;return}p.mouseInChild.value=!0,_==null||_(),{stop:_}=Be(()=>{i.openMenu(e.index,t.value)},M),Q.value&&((N=v.value.vnode.el)==null||N.dispatchEvent(new MouseEvent("mouseenter")))}},D=(d=!1)=>{var M;if(i.props.menuTrigger==="click"&&i.props.mode==="horizontal"||!i.props.collapse&&i.props.mode==="vertical"){p.mouseInChild.value=!1;return}_==null||_(),p.mouseInChild.value=!1,{stop:_}=Be(()=>!S.value&&i.closeMenu(e.index,t.value),l.value),Q.value&&d&&((M=p.handleMouseleave)==null||M.call(p,!0))};ie(()=>i.props.collapse,d=>f(!!d));{const d=N=>{m.value[N.index]=N},M=N=>{delete m.value[N.index]};Ie(`subMenu:${s.uid}`,{addSubMenu:d,removeSubMenu:M,handleMouseleave:D,mouseInChild:S,level:p.level+1})}return n({opened:R}),ce(()=>{i.addSubMenu(Y),p.addSubMenu(Y)}),xe(()=>{p.removeSubMenu(Y),i.removeSubMenu(Y)}),()=>{var d;const M=[(d=o.title)==null?void 0:d.call(o),O(Oe,{class:h.e("icon-arrow"),style:{transform:R.value?e.expandCloseIcon&&e.expandOpenIcon||e.collapseCloseIcon&&e.collapseOpenIcon&&i.props.collapse?"none":"rotateZ(180deg)":"none"}},{default:()=>ne(L.value)?O(s.appContext.components[L.value]):O(L.value)})],N=i.isMenuPopup?O(je,{ref:G,visible:R.value,effect:"light",pure:!0,offset:de.value,showArrow:!1,persistent:!0,popperClass:te.value,placement:w.value,teleported:Q.value,fallbackPlacements:X.value,transition:se.value,gpuAcceleration:!1},{content:()=>{var z;return O("div",{class:[r.m(b.value),r.m("popup-container"),te.value],onMouseenter:q=>I(q,100),onMouseleave:()=>D(!0),onFocus:q=>I(q,100)},[O("ul",{class:[r.b(),r.m("popup"),r.m(`popup-${w.value}`)],style:ae.value},[(z=o.default)==null?void 0:z.call(o)])])},default:()=>O("div",{class:h.e("title"),onClick:P},M)}):O(oe,{},[O("div",{class:h.e("title"),ref:U,onClick:P},M),O(At,{},{default:()=>{var z;return re(O("ul",{role:"menu",class:[r.b(),r.m("inline")],style:ae.value},[(z=o.default)==null?void 0:z.call(o)]),[[ve,R.value]])}})]);return O("li",{class:[h.b(),h.is("active",j.value),h.is("opened",R.value),h.is("disabled",e.disabled)],role:"menuitem",ariaHaspopup:!0,ariaExpanded:R.value,onMouseenter:I,onMouseleave:()=>D(),onFocus:I},[N])}}});const dn=ye({mode:{type:String,values:["horizontal","vertical"],default:"vertical"},defaultActive:{type:String,default:""},defaultOpeneds:{type:ge(Array),default:()=>wt([])},uniqueOpened:Boolean,router:Boolean,menuTrigger:{type:String,values:["hover","click"],default:"hover"},collapse:Boolean,backgroundColor:String,textColor:String,activeTextColor:String,closeOnClickOutside:Boolean,collapseTransition:{type:Boolean,default:!0},ellipsis:{type:Boolean,default:!0},popperOffset:{type:Number,default:6},ellipsisIcon:{type:le,default:()=>yt},popperEffect:{type:ge(String),default:"dark"},popperClass:String,showTimeout:{type:Number,default:300},hideTimeout:{type:Number,default:300}}),Ce=e=>Array.isArray(e)&&e.every(o=>ne(o)),pn={close:(e,o)=>ne(e)&&Ce(o),open:(e,o)=>ne(e)&&Ce(o),select:(e,o,n,s)=>ne(e)&&Ce(o)&&lt(n)&&(s===void 0||s instanceof Promise)};var fn=T({name:"ElMenu",props:dn,emits:pn,setup(e,{emit:o,slots:n,expose:s}){const t=Te(),v=t.appContext.config.globalProperties.$router,r=x(),h=H("menu"),i=H("sub-menu"),p=x(-1),u=x(e.defaultOpeneds&&!e.collapse?e.defaultOpeneds.slice(0):[]),m=x(e.defaultActive),_=x({}),S=x({}),U=y(()=>e.mode==="horizontal"||e.mode==="vertical"&&e.collapse),G=()=>{const l=m.value&&_.value[m.value];if(!l||e.mode==="horizontal"||e.collapse)return;l.indexPath.forEach(f=>{const P=S.value[f];P&&w(f,P.indexPath)})},w=(l,c)=>{u.value.includes(l)||(e.uniqueOpened&&(u.value=u.value.filter(f=>c.includes(f))),u.value.push(l),o("open",l,c))},L=l=>{const c=u.value.indexOf(l);c!==-1&&u.value.splice(c,1)},F=(l,c)=>{L(l),o("close",l,c)},Q=({index:l,indexPath:c})=>{u.value.includes(l)?F(l,c):w(l,c)},se=l=>{(e.mode==="horizontal"||e.collapse)&&(u.value=[]);const{index:c,indexPath:f}=l;if(!(He(c)||He(f)))if(e.router&&v){const P=l.route||c,I=v.push(P).then(D=>(D||(m.value=c),D));o("select",c,f,{index:c,indexPath:f,route:P},I)}else m.value=c,o("select",c,f,{index:c,indexPath:f})},X=l=>{const c=_.value,f=c[l]||m.value&&c[m.value]||c[e.defaultActive];f?m.value=f.index:m.value=l},R=l=>{const c=getComputedStyle(l),f=Number.parseInt(c.marginLeft,10),P=Number.parseInt(c.marginRight,10);return l.offsetWidth+f+P||0},j=()=>{var l,c;if(!r.value)return-1;const f=Array.from((c=(l=r.value)==null?void 0:l.childNodes)!=null?c:[]).filter(q=>q.nodeName!=="#comment"&&(q.nodeName!=="#text"||q.nodeValue)),P=64,I=getComputedStyle(r.value),D=Number.parseInt(I.paddingLeft,10),d=Number.parseInt(I.paddingRight,10),M=r.value.clientWidth-D-d;let N=0,z=0;return f.forEach((q,tt)=>{N+=R(q),N<=M-P&&(z=tt+1)}),z===f.length?-1:z},b=l=>S.value[l].indexPath,Y=(l,c=33.34)=>{let f;return()=>{f&&clearTimeout(f),f=setTimeout(()=>{l()},c)}};let ae=!0;const de=()=>{if(p.value===j())return;const l=()=>{p.value=-1,it(()=>{p.value=j()})};ae?l():Y(l)(),ae=!1};ie(()=>e.defaultActive,l=>{_.value[l]||(m.value=""),X(l)}),ie(()=>e.collapse,l=>{l&&(u.value=[])}),ie(_.value,G);let te;at(()=>{e.mode==="horizontal"&&e.ellipsis?te=mt(r,de).stop:te==null||te()});const pe=x(!1);{const l=I=>{S.value[I.index]=I},c=I=>{delete S.value[I.index]};Ie("rootMenu",Pe({props:e,openedMenus:u,items:_,subMenus:S,activeIndex:m,isMenuPopup:U,addMenuItem:I=>{_.value[I.index]=I},removeMenuItem:I=>{delete _.value[I.index]},addSubMenu:l,removeSubMenu:c,openMenu:w,closeMenu:F,handleMenuItemClick:se,handleSubMenuClick:Q})),Ie(`subMenu:${t.uid}`,{addSubMenu:l,removeSubMenu:c,mouseInChild:pe,level:0})}return ce(()=>{e.mode==="horizontal"&&new sn(t.vnode.el,h.namespace.value)}),s({open:c=>{const{indexPath:f}=S.value[c];f.forEach(P=>w(P,f))},close:L,handleResize:de}),()=>{var l,c;let f=(c=(l=n.default)==null?void 0:l.call(n))!=null?c:[];const P=[];if(e.mode==="horizontal"&&r.value){const M=$t(f),N=p.value===-1?M:M.slice(0,p.value),z=p.value===-1?[]:M.slice(p.value);z!=null&&z.length&&e.ellipsis&&(f=N,P.push(O(Ae,{index:"sub-menu-more",class:i.e("hide-arrow"),popperOffset:e.popperOffset},{title:()=>O(Oe,{class:i.e("icon-more")},{default:()=>O(e.ellipsisIcon)}),default:()=>z})))}const I=Qe(e,0),D=e.closeOnClickOutside?[[ft,()=>{u.value.length&&(pe.value||(u.value.forEach(M=>o("close",M,b(M))),u.value=[]))}]]:[],d=re(O("ul",{key:String(e.collapse),role:"menubar",ref:r,style:I.value,class:{[h.b()]:!0,[h.m(e.mode)]:!0,[h.m("collapse")]:e.collapse}},[...f,...P]),D);return e.collapseTransition&&e.mode==="vertical"?O(rn,()=>d):d}}});const mn=ye({index:{type:ge([String,null]),default:null},route:{type:ge([String,Object])},disabled:Boolean}),vn={click:e=>ne(e.index)&&Array.isArray(e.indexPath)},ke="ElMenuItem",hn=T({name:ke,components:{ElTooltip:je},props:mn,emits:vn,setup(e,{emit:o}){const n=Te(),s=he("rootMenu"),t=H("menu"),v=H("menu-item");s||_e(ke,"can not inject root menu");const{parentMenu:r,indexPath:h}=Ze(n,rt(e,"index")),i=he(`subMenu:${r.value.uid}`);i||_e(ke,"can not inject sub menu");const p=y(()=>e.index===s.activeIndex),u=Pe({index:e.index,indexPath:h,active:p}),m=()=>{e.disabled||(s.handleMenuItemClick({index:e.index,indexPath:h.value,route:e.route}),o("click",u))};return ce(()=>{i.addSubMenu(u),s.addMenuItem(u)}),xe(()=>{i.removeSubMenu(u),s.removeMenuItem(u)}),{parentMenu:r,rootMenu:s,active:p,nsMenu:t,nsMenuItem:v,handleClick:m}}});function gn(e,o,n,s,t,v){const r=Re("el-tooltip");return g(),$("li",{class:E([e.nsMenuItem.b(),e.nsMenuItem.is("active",e.active),e.nsMenuItem.is("disabled",e.disabled)]),role:"menuitem",tabindex:"-1",onClick:e.handleClick},[e.parentMenu.type.name==="ElMenu"&&e.rootMenu.props.collapse&&e.$slots.title?(g(),K(r,{key:0,effect:e.rootMenu.props.popperEffect,placement:"right","fallback-placements":["left"],persistent:""},{content:k(()=>[B(e.$slots,"title")]),default:k(()=>[C("div",{class:E(e.nsMenu.be("tooltip","trigger"))},[B(e.$slots,"default")],2)]),_:3},8,["effect"])):(g(),$(oe,{key:1},[B(e.$slots,"default"),B(e.$slots,"title")],64))],10,["onClick"])}var Xe=W(hn,[["render",gn],["__file","menu-item.vue"]]);const _n={title:String},bn="ElMenuItemGroup",yn=T({name:bn,props:_n,setup(){return{ns:H("menu-item-group")}}});function Mn(e,o,n,s,t,v){return g(),$("li",{class:E(e.ns.b())},[C("div",{class:E(e.ns.e("title"))},[e.$slots.title?B(e.$slots,"title",{key:1}):(g(),$(oe,{key:0},[ue(Z(e.title),1)],64))],2),C("ul",null,[B(e.$slots,"default")])],2)}var Ye=W(yn,[["render",Mn],["__file","menu-item-group.vue"]]);const wn=be(fn,{MenuItem:Xe,MenuItemGroup:Ye,SubMenu:Ae}),Cn=ee(Xe);ee(Ye);const kn=ee(Ae),In="/assets/logoImg-CwUB_hdd.png",En={class:"h100"},$n={class:"LogoBlock"},Sn=["src"],Tn={__name:"AsidePage",setup(e){const o=Fe(),n=ut(),s=De();let t=x("");const{t:v}=ct(),r=[{path:"/internet",name:"aside.internet",icon:"icon-diqiu"},{path:"/cellularNetwork",name:"aside.cellularNetwork",icon:"icon-wuxiandianbo"},{path:"/wireless",name:"aside.wireless",icon:"icon-WIFI"},{path:"/client",name:"aside.client",icon:"icon-devices"},{path:"/application",name:"Application.Application",icon:"icon-a-office-tools-qrcode"},{path:"/smsconversation",name:"tools.SMSConversation",icon:"icon-duanxin"},{path:"/systemlog",name:"log.title",icon:"icon-jilu"}],h=(u,m)=>u.find(S=>S.path===m)!=null,i=u=>{if(s.setHandlePath(u.path),s.setHandlePathName(v(u.name)),s.handleTwoPathName!==""&&s.setHandleTwoPathName(""),t.value===u.path){o.go(0);return}t.value=u.path,o.push(u.path)};ce(()=>{t.value=s.handlePath||"/",o.push(t.value)});const p=(u,m)=>{for(let _ of u){if(_.path===m)return _.path;if(_.children){let S=p(_.children,m);if(S)return S}}return null};return ie(()=>[n.path,n.name],([u,m])=>{t.value=p(r,u),s.setHandlePath(u),s.setHandlePathName(m)}),(u,m)=>{const _=Cn,S=kn,U=wn,G=Ke;return g(),$("div",En,[A(G,{class:"layout-aside"},{default:k(()=>[C("div",$n,[C("img",{src:a(In),alt:""},null,8,Sn)]),A(U,{"default-active":a(t),mode:"vertical"},{default:k(()=>[(g(),$(oe,null,Le(r,w=>(g(),$(oe,null,[w.children&&w.children.length>0?(g(),K(S,{index:w.path,key:w.path},{title:k(()=>[C("i",{class:E(["iconfont",w.icon,{isactive:h(w.children,a(t))}]),style:{"margin-right":"20px"}},null,2),C("div",{class:E({isactive:h(w.children,a(t))})},Z(u.$t(w.name)),3)]),default:k(()=>[(g(!0),$(oe,null,Le(w.children,L=>(g(),K(_,{class:"h70",index:L.path,key:L.path,onClick:F=>i(L)},{default:k(()=>[C("i",{class:E(["iconfont",L.icon]),style:{"margin-right":"20px"}},null,2),ue(" "+Z(u.$t(L.name)),1)]),_:2},1032,["index","onClick"]))),128))]),_:2},1032,["index"])):(g(),K(_,{class:"h70",index:w.path,key:w.path,onClick:L=>i(w)},{default:k(()=>[C("i",{class:E(["iconfont",w.icon]),style:{"margin-right":"20px"}},null,2),ue(" "+Z(u.$t(w.name)),1)]),_:2},1032,["index","onClick"]))],64))),64))]),_:1},8,["default-active"])]),_:1})])}}},et=Ne(Tn,[["__scopeId","data-v-75787630"]]),Pn=e=>(Ve("data-v-230a97dd"),e=e(),We(),e),xn={class:"HeaderPage"},Nn={class:"HeaderBlock"},On={class:"HeaderLeft"},An={class:"rightName"},Bn=Pn(()=>C("svg",{xmlns:"http://www.w3.org/2000/svg",width:"24",height:"24",viewBox:"0 0 24 24",fill:"none"},[C("path",{d:"M11.9958 3H3V21H12",stroke:"#333333","stroke-width":"2","stroke-linecap":"round","stroke-linejoin":"round"}),C("path",{d:"M16.5 16.5L21 12L16.5 7.5",stroke:"#333333","stroke-width":"2","stroke-linecap":"round","stroke-linejoin":"round"}),C("path",{d:"M8 11.9958H21",stroke:"#333333","stroke-width":"2","stroke-linecap":"round","stroke-linejoin":"round"})],-1)),Hn=[Bn],Ln={__name:"HeaderPage",setup(e){const o=De(),n=x(window.innerWidth<1056);let s=x(!1);const t=Fe(),v=()=>{o.setHandleTwoPathName(""),t.go(0)},r=()=>{let p=localStorage.getItem("lang")||"en";localStorage.clear(),setTimeout(()=>{localStorage.setItem("lang",p),t.push("/login")},1e3)},h=()=>{n.value=window.innerWidth<1056};ce(()=>{h(),window.addEventListener("resize",h)}),xe(()=>{window.removeEventListener("resize",h)});const i=()=>{s.value=!0};return(p,u)=>{const m=tn;return g(),$("div",xn,[C("div",Nn,[C("div",On,[n.value?(g(),$("i",{key:0,class:"iconfont icon-Click-icons-",onClick:i})):J("",!0),ue("  "),C("div",{class:E(["leftName",{greyName:a(o).handleTwoPathName!=""}]),onClick:v},[ue(Z(p.$t(a(o).handlePathName))+" ",1),re(C("span",null,Z("/"),512),[[ve,a(o).handleTwoPathName!=""]])],2),re(C("div",An,[C("span",null,Z(""+p.$t(a(o).handleTwoPathName)),1)],512),[[ve,a(o).handleTwoPathName!=""]])]),C("div",{class:"HearderRight"},[C("div",{onClick:r},Hn)])]),A(m,{title:"我是标题",size:"60%",modelValue:a(s),"onUpdate:modelValue":u[0]||(u[0]=_=>dt(s)?s.value=_:s=_),"with-header":!1,direction:"ltr"},{default:k(()=>[A(et)]),_:1},8,["modelValue"])])}}},zn=Ne(Ln,[["__scopeId","data-v-230a97dd"]]),Rn=e=>(Ve("data-v-3729c60d"),e=e(),We(),e),Fn={class:"HomePage"},Dn=Rn(()=>C("div",{class:"footerRight"},"Copyright © 2024 JTUN. All Rights Reserved.",-1)),Vn={__name:"HomePage",setup(e){let o=x(!1);return pt(()=>{document.body.clientWidth<1e3?o.value=!0:o.value=!1}),(n,s)=>{const t=Ke,v=Jt,r=Re("router-view"),h=Kt,i=Gt,p=Ut;return g(),$("div",Fn,[A(p,null,{default:k(()=>[a(o)?J("",!0):(g(),K(t,{key:0,width:"240px"},{default:k(()=>[A(et)]),_:1})),A(p,{class:"main"},{default:k(()=>[A(v,null,{default:k(()=>[A(zn)]),_:1}),A(h,null,{default:k(()=>[A(r)]),_:1}),A(i,null,{default:k(()=>[Dn]),_:1})]),_:1})]),_:1})])}}},so=Ne(Vn,[["__scopeId","data-v-3729c60d"]]);export{so as default};