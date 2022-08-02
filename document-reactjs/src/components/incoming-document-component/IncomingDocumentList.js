import React, {useEffect, useState} from 'react';
import axios from "axios";
import {Link} from "react-router-dom";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Input, Label} from "reactstrap";
import {MDBTable, MDBTableBody, MDBTableHead} from "mdb-react-ui-kit";
import Pagination from "react-bootstrap/Pagination";
import Modal from "react-bootstrap/Modal";
import {AutoDismissFlag, FlagGroup} from "@atlaskit/flag";
import SuccessIcon from "@atlaskit/icon/glyph/check-circle";
import {token} from "@atlaskit/tokens";
import {G400} from "@atlaskit/theme/colors";

const IncomingDocumentList = (props) => {
    const searchParamForm = {
        excerpt: '',
        serialNumber: '',
        fromDate: '2000-01-01 00:00:00',
        toDate: '3000-01-01 23:59:59',
        signerName: '',
        pageNumber: 0,
        elementNumber: 4
    }

    const [documentList, setDocumentList] = useState([]);
    const [searchParam, setSearchParam] = useState(searchParamForm);
    const [searchParamVal, setSearchParamVal] = useState(searchParamForm);
    const [page, setPage] = useState();
    const [totalPage, setTotalPage] = useState();
    const [deleteDocument, setdeleteDocument] = useState({'id': 0, 'excerpt': ''});
    const [show, setShow] = useState(false);
    const [deleteSuccess, setDeleteSuccess] = useState([]);

    useEffect(() => {
        axios.post('http://localhost:8080/api/incoming-document/list-dto', searchParam)
            .then((data) => {
                setDocumentList(data.data.data);
                setPage(data.data.page);
                setTotalPage(Math.ceil(data.data.totalElement / data.data.numberOfElementPerPage));
            })
    }, [searchParam]);

    const searchHandleChange = (event) => {
        const {name, value} = event.target;
        if (name === "fromDate" || name === "toDate") {
            const dateValue = new Intl.DateTimeFormat('en-ZA', {
                month: '2-digit',
                day: '2-digit',
                year: 'numeric',
                minute: '2-digit',
                hour: '2-digit',
                second: '2-digit',
            }).format(new Date(value)).replace(',', '').replace('/', '-').replace('/', '-');
            setSearchParamVal({...searchParamVal, [name]: dateValue});
        } else {
            setSearchParamVal({...searchParamVal, [name]: value});
        }
    }

    const search = () => {
        setSearchParam(searchParamVal);
    }

    const resetSearch = () => {
        setSearchParam(searchParamForm)
    }

    const previousPage = (event) => {
        setSearchParam({...searchParam, pageNumber: page - 1});
    };

    const nextPage = (event) => {
        setSearchParam({...searchParam, pageNumber: page + 1});
    };

    const callComfirmDeleteModal = (id, excerpt) => {
        setdeleteDocument({id: id, excerpt: excerpt});
        handleConfirmDeleteModalShow();
    }

    const handleConfirmDeleteModalClose = () => setShow(false);
    const handleConfirmDeleteModalShow = () => setShow(true);

    const handleDeleteDocument = () => {
        axios.post('http://localhost:8080/api/incoming-document/delete', {id: deleteDocument.id})
            .then((data) => {
                setDeleteSuccess(data.data.message);
                setdeleteDocument({id: 0, excerpt: ''});
                setSearchParam(searchParamForm);
                handleConfirmDeleteModalClose();
                addFlag.call();
            })
    }

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

    const documents = documentList.map(document => {
        return <tr key={document.id}>
            <td>{document.id}</td>
            <td>{document.excerpt}</td>
            <td>{document.serialNumber}</td>
            <td>
                {new Intl.DateTimeFormat('en-GB', {
                    month: '2-digit',
                    day: '2-digit',
                    year: 'numeric',
                    minute: '2-digit',
                    hour: '2-digit',
                    second: '2-digit',
                }).format(new Date(document.signingDate))}
            </td>
            <td>{document.signerName}</td>
            <td>
                <Link to={"/document-edit/" + document.id}>
                    <Button size="sm" variant="outline-warning">Sửa</Button>
                </Link>
                <Button size="sm" variant="outline-danger"
                        onClick={() => callComfirmDeleteModal(document.id, document.excerpt)}>Xóa</Button>
            </td>
        </tr>
    })

    return (
        <>
            <Row>
                <Col lg={{span: 4, offset: 1}}>
                    <h3>Danh sách văn bản đến</h3>
                </Col>
                <Col lg={{span: 2, offset: 1}}>
                    <Label for="excerptSearch">Trích yếu</Label>
                    <Input type="text" name="excerpt" id="excerptSearch"
                           onChange={searchHandleChange}/>

                    <Label for="serialNumberSearch">Số hiệu văn bản</Label>
                    <Input type="text" name="serialNumber" id="serialNumberSearch"
                           onChange={searchHandleChange}/>

                    <Label for="fromDateSearch">Từ ngày</Label>
                    <Input type="datetime-local" name="fromDate" id="fromDateSearch"
                           onChange={searchHandleChange} />

                    <Label for="toDateSearch">Đến ngày</Label>
                    <Input type="datetime-local" name="toDate" id="toDateSearch"
                           onChange={searchHandleChange}/>
                    <Label for="signerNameSearch">Người ký</Label>
                    <Input type="text" name="signerName" id="signerNameSearch"
                           onChange={searchHandleChange}/>
                </Col>
                <Col lg={{span: 2, offset: 1}}>
                    <Button size="sm" variant="primary" type="submit" onClick={search}>
                        Tìm kiếm
                    </Button>
                    <Button size="sm" variant="primary" type="submit" onClick={resetSearch}>
                        Reset
                    </Button>
                </Col>

            </Row>
            <Row className="justify-content-md-center">
                <Col md="auto">
                    <Link to={"/document-add"}>
                        <Button size="sm" variant="outline-success">Thêm mới</Button>
                    </Link>
                    <Link to={"/document-amount-receiver-list"}>
                        <Button size="sm" variant="outline-success">Thống kê danh sách văn bản nhận được theo người</Button>
                    </Link>
                    <MDBTable striped hover>
                        <MDBTableHead>
                            <tr>
                                <th scope='col'>Id</th>
                                <th scope='col'>Trích yếu</th>
                                <th scope='col'>Số hiệu văn bản</th>
                                <th scope='col'>Ngày ký</th>
                                <th scope='col'>Người ký</th>
                                <th scope='col'>Hành động</th>
                            </tr>
                        </MDBTableHead>
                        <MDBTableBody>
                            {documents}
                        </MDBTableBody>
                    </MDBTable>
                </Col>
            </Row>
            <Row>
                <Col></Col>
                <Col md="auto"></Col>
                <Col xs lg="3">
                    <Pagination>
                        {
                            page > 0
                                ? <Pagination.Prev onClick={previousPage}/>
                                : <Pagination.Prev disabled/>
                        }
                        <Pagination.Item>{page + 1}</Pagination.Item>
                        <Pagination.Item>{"/"}</Pagination.Item>
                        <Pagination.Item>{totalPage}</Pagination.Item>
                        {
                            page < totalPage - 1
                                ? <Pagination.Next onClick={nextPage}/>
                                : <Pagination.Next disabled/>
                        }
                    </Pagination>
                </Col>
            </Row>

            <Modal
                show={show}
                onHide={handleConfirmDeleteModalClose}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header>
                    <Modal.Title>Xác nhận</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Bạn chắc chắn muốn xóa văn bản <span className="text-danger">{deleteDocument.excerpt}</span>.
                    <p className="text-warning"> Lưu ý: Thao tác này không thể hoàn tác </p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleConfirmDeleteModalClose}>
                        Đóng
                    </Button>
                    <Button variant="danger" onClick={() => handleDeleteDocument()}>Xóa</Button>
                </Modal.Footer>
            </Modal>

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
                            title={deleteSuccess}
                        />
                    );
                })}
            </FlagGroup>
        </>
    );
}

export default IncomingDocumentList;