
const defaultState = {
    red: 0,
    blue: 0
}

///// 版本1: 在一个掌管一切的reducer中写很多if else
// export default (state = defaultState, action) => {
//     switch (action.type) {
//         case 'ADD_RED':
//             return Object.assign({}, state, { red: state.red + 1 });
//         case 'SUB_RED':
//             return Object.assign({}, state, { red: state.red - 1 });
//         case 'ADD_BLUE':
//             return Object.assign({}, state, { red: state.blue + 1 });
//         case 'SUB_BLUE':
//             return Object.assign({}, state, { red: state.blue - 1 });
//         default:
//             return state;
//     }
// }

///// 版本2: 分成多个【子reducer】，每个【子reducer】只处理state的一个属性
// export default (state = defaultState, action) => {
//     return {
//         red: red(state.red, action),
//         blue: blue(state.blue, action)
//     }
// }

// 子reducer
function red(redValue, action) {
    switch (action.type) {
        case 'ADD_RED':
            return redValue + 1;
        case 'SUB_RED':
            return redValue - 1;
        default:
            return redValue;
    }
}

// 子reducer
function blue(blueValue, action) {
    switch (action.type) {
        case 'ADD_BLUE':
            return blueValue + 1;
        case 'SUB_BLUE':
            return blueValue - 1;
        default:
            return blueValue;
    }
}

///// 版本3：定义combineReducers函数，把子reducer组合起来

// 入参：reducers。其key为state属性名，value为[子reducer]。
// 返回值：组合后的reducer。
const combineReducers = (reducers, defaultState) => {
    // reducer接收完整的state和action为入参; 返回新的state
    return (state = defaultState, action) => {
        return Object.keys(reducers).reduce(
            (newState, key ) => {
                newState[key] = reducers[key]/*子reducer*/(state[key], action);
                return newState; 
            },
            {} );
    }
}

export default combineReducers({red, blue}, defaultState)

///// 以下为JavaScript语法

// 在ES6中， {red, blue}等价于{red:red, blue:blue}

/*
// Examples:Object.keys(obj)
// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/keys

// simple array
var arr = ['a', 'b', 'c'];
console.log(Object.keys(arr)); // console: ['0', '1', '2']

// array like object
var obj = { 0: 'a', 1: 'b', 2: 'c' };
console.log(Object.keys(obj)); // console: ['0', '1', '2']

// array like object with random key ordering
var anObj = { 100: 'a', 2: 'b', 7: 'c' };
console.log(Object.keys(anObj)); // console: ['2', '7', '100']
*/


/*
// JavaScript Demo: Array.reduce()

const array1 = [1, 2, 3, 4];
const reducer = (accumulator, currentValue) => accumulator + currentValue;

// 1 + 2 + 3 + 4
console.log(array1.reduce(reducer));
// expected output: 10

// 5 + 1 + 2 + 3 + 4
console.log(array1.reduce(reducer, 5));
// expected output: 15
*/