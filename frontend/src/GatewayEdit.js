import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label, ButtonGroup, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';

class GatewayEdit extends Component {

    emptyItem = {
        name: '',
        ipv4Address: '',
        devices: []
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const gateway = await (await fetch(`/api/gateways/${this.props.match.params.id}`)).json();
            this.setState({item: gateway});
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        await fetch('/api/gateways' + (item.id ? '/' + item.id : ''), {
            method: (item.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
        this.props.history.push('/');
    }

    async removeDevice(gatewayId, deviceId) {
        await fetch(`/api/gateways/${gatewayId}/devices/${deviceId}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedDevices = [...this.state.item.devices].filter(i => i.id !== deviceId);
            let item = {...this.state.item};
            item['devices'] = updatedDevices;
            this.setState({item});
        });
    }

    render() {
        const {item} = this.state;
        const renderEditGateway = () => {
            const deviceList = item.devices.map(device => {
                return <tr key={device.id}>
                    <td style={{whiteSpace: 'nowrap'}}>{device.vendor}</td>
                    <td>{device.status}</td>
                    <td>{device.creationDateTime}</td>
                    <td>
                        <ButtonGroup>
                            <Button size="sm" color="danger" onClick={() => this.removeDevice(item.id, device.id)}>Delete</Button>
                        </ButtonGroup>
                    </td>
                </tr>
            });
            return <div>
                <AppNavbar/>
                <Container>
                    <h2>Edit Gateway</h2>
                    <Label for="name">Name</Label>
                    <Input type="text" name="name" id="name" value={item.name || ''} readOnly={true}/>
                    <Label for="ipv4Address">IP Address</Label>
                    <Input type="text" name="ipv4Address" id="ipv4Address" value={item.ipv4Address || ''} readOnly={true}/>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Vendor</th>
                            <th width="30%">Status</th>
                            <th width="30%">Create Date Time</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {deviceList}
                        </tbody>
                    </Table>
                    <Button color="primary" tag={Link} to="/">Done</Button>
                </Container>
            </div>;
        };

        const renderAddGateway = () => {
            return <div>
                <AppNavbar/>
                <Container>
                    <h2>Add Gateway</h2>
                    <Form onSubmit={this.handleSubmit}>
                        <FormGroup>
                            <Label for="name">Name</Label>
                            <Input type="text" name="name" id="name" value={item.name || ''}
                                   onChange={this.handleChange} autoComplete="name"/>
                        </FormGroup>
                        <FormGroup>
                            <Label for="ipv4Address">IP Address</Label>
                            <Input type="text" name="ipv4Address" id="ipv4Address" value={item.ipv4Address || ''}
                                   onChange={this.handleChange} autoComplete="ipv4Address"/>
                        </FormGroup>
                        <FormGroup>
                            <Button color="primary" type="submit">Save</Button>{' '}
                            <Button color="secondary" tag={Link} to="/">Cancel</Button>
                        </FormGroup>
                    </Form>
                </Container>
            </div>
        }

        return item.id ? renderEditGateway() : renderAddGateway();
    }
}

export default withRouter(GatewayEdit);
