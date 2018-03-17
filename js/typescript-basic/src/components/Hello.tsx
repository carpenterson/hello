import * as React from "react";

interface HelloState {
    msg: string;
}

export class Hello extends React.Component<{}, HelloState>{

    constructor() {
        super({});

        let list: number[] = [1, 2, 3];
        let list2: Array<number> = [1, 2, 3];
        let x: any;
        x = ["Hello", 10];

        this.state = {
            msg: "Hello, Tom!" + x[0].substr(1)
        }
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick = () => {
        let isDone: boolean = false;
        let decimal: number = 6;
        let hex: number = 0xf00d;
        let color: string = "blue";
        let fullName = 'Bob Bobbington';
        let age = 37;
        let sentence: string = `Hello, my name is ${fullName}.
        \n
        I'll be ${age + 1} years old next month.`;

        this.setState({ msg: sentence });
    }

    // label不能设置name属性
    // https://doc.react-china.org/blog/2017/09/08/dom-attributes-in-react-16.html
    render() {
        return (
            <div>
                <p>{this.state.msg}</p>
                <button onClick={this.handleClick}>点击我</button>
                <label id={'a'}>a</label>
            </div>
        )
    }
}