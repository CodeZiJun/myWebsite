import router from "@/router";

function getNames(name, routes) {
    let names = []
    while(true) {
        names.push(name)
        let route = getRouteByName(name, routes)
        let parentRoute = getRouteByName(route.meta?.parentRouteName, routes)
        if(parentRoute) {
            name = parentRoute.name
        } else {
            break
        }
    }
    return names.reverse()
}

function getRouteByName(name, routes) {
    for(let route of routes) {
        if(route.name === name){
            return route
        }
    }
    return undefined
}

function getDescriptions(names, routes) {
    let descriptions = []
    for(let name of names) {
        let route = getRouteByName(name, routes)
        descriptions.push(route.meta?.description)
    }
    return descriptions
}

export {getNames, getDescriptions}