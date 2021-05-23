import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label, ButtonGroup, Table } from 'reactstrap';
import Select from 'react-select'
import AppNavbar from './AppNavbar';

const vendorStatusOptions = [
    { value: 'ONLINE', label: 'Online' },
    { value: 'OFFLINE', label: 'Offline' }
];

class GatewayEdit extends Component {

    emptyItem = {
        name: '',
        ipv4Address: '',
        devices: []
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem,
            newDeviceVendor: '',
            newDeviceStatus: 'ONLINE',
            showSuccessMessage: false,
            errorMessage: ''
        };
        this.handleChangeItem = this.handleChangeItem.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const gateway = await (await fetch(`/api/gateways/${this.props.match.params.id}`)).json();
            this.setState({item: gateway});
        }
    }

    handleChangeItem(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item: item});
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
        }).then(response => response.json()).then(response => {
            const errMsg = (response.errors && response.errors[0]) ? response.errors[0] : response.message;
            if(errMsg) {
                this.setState({errorMessage: errMsg});
            } else {
                this.setState({showSuccessMessage: true})
            }
        });
    }

    async addDevice(gatewayId, newDeviceVendor, newDeviceStatus) {
        const updateDevicesInState = (device) => {
            let updatedDevices = [...this.state.item.devices];
            updatedDevices.push(device);
            let item = {...this.state.item};
            item['devices'] = updatedDevices;
            this.setState({item: item, newDeviceVendor: '', newDeviceStatus: 'ONLINE'});
        };
        if (gatewayId) {
            const newDevice = {vendor: newDeviceVendor, status: newDeviceStatus};
            await fetch(`/api/gateways/${gatewayId}/devices`, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newDevice)
            }).then(response => response.json()).then(response => {
                const errMsg = (response.errors && response.errors[0]) ? response.errors[0] : response.message;
                if(errMsg) {
                    this.setState({errorMessage: errMsg});
                } else {
                    updateDevicesInState(response);
                }
            });
        } else {
            const randomNumber = Math.floor(Math.random() * 10000) + 1 ;
            const newDevice = {id: randomNumber, vendor: newDeviceVendor, status: newDeviceStatus};
            updateDevicesInState(newDevice);
        }
    }

    async removeDevice(gatewayId, deviceId) {
        const updateDevicesInState = () => {
            let updatedDevices = [...this.state.item.devices].filter(i => i.id !== deviceId);
            let item = {...this.state.item};
            item['devices'] = updatedDevices;
            this.setState({item: item})
        };
        if (gatewayId) {
            await fetch(`/api/gateways/${gatewayId}/devices/${deviceId}`, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }).then(() => {
                updateDevicesInState();
            });
        } else {
            updateDevicesInState();
        }
    }

    handleNewDeviceVendorChange(value) {
        this.setState({ newDeviceVendor: value })
    }

    handleNewDeviceStatusChange(value) {
        this.setState({ newDeviceStatus: value})
    }

    render() {
        const {item} = this.state;
        const {newDeviceVendor} = this.state;
        const {newDeviceStatus} = this.state;
        const RenderEditGateway = () => {
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
                    <Label for="newDeviceVendor">Device Vendor</Label>
                    <Input type="text" value={newDeviceVendor} onChange={event => this.handleNewDeviceVendorChange(event.target.value)} />
                    <Label for="newDeviceStatus">Device Status</Label>
                    <Select options={vendorStatusOptions} defaultValue={vendorStatusOptions[0]} value={vendorStatusOptions.filter(opt => opt.value === newDeviceStatus)} onChange={event => this.handleNewDeviceStatusChange(event.value)} />
                    <Button color="primary" onClick={() => this.addDevice(item.id, newDeviceVendor, newDeviceStatus)}>Add Device</Button>
                    <Button color="secondary" tag={Link} to="/">Done</Button>
                    { this.state.errorMessage && <h3 className="error"> { this.state.errorMessage } </h3> }
                    { this.state.showSuccessMessage && <h3 className="text-success">Operation successful</h3> }
                </Container>
            </div>;
        };
        const RenderAddGateway = () => {
            const deviceList = item.devices.map(device => {
                return <tr key={device.id}>
                    <td style={{whiteSpace: 'nowrap'}}>{device.vendor}</td>
                    <td>{device.status}</td>
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
                    <h2>Add Gateway</h2>
                    <Form onSubmit={this.handleSubmit}>
                        <FormGroup>
                            <Label for="name">Name</Label>
                            <Input type="text" name="name" id="name" value={item.name || ''}
                                   onChange={this.handleChangeItem} autoComplete="name"/>
                        </FormGroup>
                        <FormGroup>
                            <Label for="ipv4Address">IP Address</Label>
                            <Input type="text" name="ipv4Address" id="ipv4Address" value={item.ipv4Address || ''} onChange={this.handleChangeItem} autoComplete="ipv4Address"/>
                        </FormGroup>
                        <FormGroup>
                            <Table className="mt-4">
                                <thead>
                                <tr>
                                    <th width="30%">Vendor</th>
                                    <th width="30%">Status</th>
                                    <th width="40%">Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                {deviceList}
                                </tbody>
                            </Table>
                            <Label for="newDeviceVendor">Device Vendor</Label>
                            <Input type="text" value={newDeviceVendor} onChange={event => this.handleNewDeviceVendorChange(event.target.value)} />
                            <Label for="newDeviceStatus">Device Status</Label>
                            <Select options={vendorStatusOptions} defaultValue={vendorStatusOptions[0]} value={vendorStatusOptions.filter(opt => opt.value === newDeviceStatus)} onChange={event => this.handleNewDeviceStatusChange(event.value)} />
                            <Button color="primary" onClick={() => this.addDevice(item.id, newDeviceVendor, newDeviceStatus)}>Add Device</Button>
                        </FormGroup>
                        <FormGroup>
                            <Button color="primary" type="submit">Save</Button>{' '}
                            <Button color="secondary" tag={Link} to="/">Done</Button>
                            { this.state.errorMessage && <h3 className="error"> { this.state.errorMessage } </h3> }
                            { this.state.showSuccessMessage && <h3 className="text-success">Operation successful</h3> }
                        </FormGroup>
                    </Form>
                </Container>
            </div>
        };
        return this.state.item.id ? <RenderEditGateway /> : <RenderAddGateway />;
    }
}

export default withRouter(GatewayEdit);
