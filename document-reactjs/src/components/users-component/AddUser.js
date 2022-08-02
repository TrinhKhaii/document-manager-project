import React, {useState} from 'react';
import {Link} from 'react-router-dom';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import {Form, FormGroup, Input, Label, FormText} from 'reactstrap';
import Button from 'react-bootstrap/Button';
import SuccessIcon from '@atlaskit/icon/glyph/check-circle';
import {G400} from '@atlaskit/theme/colors';
import {token} from '@atlaskit/tokens';
import {AutoDismissFlag, FlagGroup} from '@atlaskit/flag';
import axios from "axios";

const AddUser = (props) => {

    const addUserForm = {
        userName: ''
    }
    const [user, setUser] = useState(addUserForm);
    const [error, setError] = useState(addUserForm);
    const [success, setSuccess] = useState([]);

    const handleChange = (event) => {
        const {name, value} = event.target;
        setUser({...user, [name]: value});
        setError(addUserForm);
    }

    const submit =  (event) => {
        event.preventDefault();
         axios.post("http://localhost:8080/api/users/save", user)
            .then((data) => {
                setSuccess(data.data.message);
                setUser(addUserForm);
                setError(addUserForm);
                addFlag.call()

                 axios.post("http://localhost:8080/notification", {
                    'title': 'User manager',
                    'message': data.data.message,
                    'to': props.token
                }).then((data) => {
                    console.log(data)
                })

            }).catch((error) => {
             console.log(error)
                // setError(error.response.error);
            })
    };

    const [flags, setFlags] = React.useState([]);

    const addFlag = () => {
        const newFlagId = flags.length + 1;
        const newFlags = flags.slice();
        newFlags.splice(0, 0, newFlagId);
        setFlags(newFlags);
    };

    const handleDismiss = () => {
        setFlags(flags.slice(1));
    };

    return (
        <>
            <Row className="justify-content-md-center">
                <Col md="auto">
                    <h3>Thêm mới người dùng</h3>
                    <Form onSubmit={submit}>

                        <FormGroup>
                            <Label for="userName">Name</Label>
                            <Input type="text" name="userName" id="userName" value={user.userName || ''}
                                   onChange={handleChange} autoComplete="userName"/>
                            <FormText className={"text-danger"}>
                                {error.userName}
                            </FormText>
                        </FormGroup>
                        <Link to={"/user-list"}>
                            <Button size="sm" variant="danger">Trở về</Button>
                        </Link>
                        <Button size="sm" variant="primary" type="submit">
                            Thêm mới
                        </Button>
                    </Form>
                </Col>
            </Row>

            <FlagGroup onDismissed={handleDismiss}>
                {flags.map((flagId) => {
                    return (
                        <AutoDismissFlag
                            appearance="success"
                            id={flagId}
                            icon={
                                <SuccessIcon
                                    label="Success"
                                    secondaryColor={token('color.background.success.bold', G400)}
                                />
                            }
                            key={flagId}
                            title={success}
                        />
                    );
                })}
            </FlagGroup>
        </>
    );
}

export default AddUser;