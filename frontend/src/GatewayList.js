import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class GatewayList extends Component {

    constructor(props) {
        super(props);
        this.state = {gateways: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('/api/gateways')
            .then(response => response.json())
            .then(data => this.setState({gateways: data}));
    }

    async remove(id) {
        await fetch(`/api/gateways/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedGateways = [...this.state.gateways].filter(i => i.id !== id);
            this.setState({gateways: updatedGateways});
        });
    }
    
    render() {
        const {gateways, isLoading} = this.state;
    
        if (isLoading) {
            return <p>Loading...</p>;
        }
    
        const gatewayList = gateways.map(gateway => {
            return <tr key={gateway.id}>
                <td style={{whiteSpace: 'nowrap'}}>{gateway.name}</td>
                <td>{gateway.ipv4Address}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/gateways/" + gateway.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(gateway.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });
    
        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/gateways/new">Add Gateway</Button>
                    </div>
                    <h3>Gateways</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="30%">IP Address</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {gatewayList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}
export default GatewayList;
